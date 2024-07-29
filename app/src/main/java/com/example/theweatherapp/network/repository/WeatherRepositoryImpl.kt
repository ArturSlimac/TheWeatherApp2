package com.example.theweatherapp.network.repository

import android.annotation.SuppressLint
import android.location.Location
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.mappers.toCurrentEntity
import com.example.theweatherapp.domain.mappers.toCurrentUnitsEntity
import com.example.theweatherapp.domain.mappers.toEntity
import com.example.theweatherapp.domain.mappers.toHourlyEntity
import com.example.theweatherapp.domain.mappers.toHourlyUnitsEntity
import com.example.theweatherapp.domain.mappers.toWeatherModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.WeatherDao
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Response
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl
    @Inject
    constructor(
        private val weatherService: WeatherService,
        private val weatherDao: WeatherDao,
        private val cityRepository: CityRepository,
        private val locationProviderClient: FusedLocationProviderClient,
    ) : WeatherRepository {
        override fun getWeather(
            latitude: Double?,
            longitude: Double?,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
        ): Flow<Response<WeatherModel>> =
            flow {
                emit(Response.Loading)
                try {
                    val location = getLocation(latitude, longitude)
                    val cityName = getCityName(location!!.latitude, location.longitude)
                    val responseApi =
                        fetchWeatherFromApi(
                            location.latitude,
                            location.longitude,
                            temperatureUnit,
                            windSpeedUnit,
                            timezone,
                            cityName,
                        )

                    clearOldWeatherData()
                    saveWeatherData(responseApi)

                    emit(Response.Success(responseApi))
                } catch (e: Exception) {
                    handleException(e)
                    emitCachedWeatherIfAvailable()
                }
            }.flowOn(Dispatchers.IO)

        private suspend fun getLocation(
            latitude: Double?,
            longitude: Double?,
        ): Location? =
            if (latitude == null || longitude == null) {
                getCurrentLocation()
            } else {
                Location("").apply {
                    this.latitude = latitude
                    this.longitude = longitude
                }
            }

        private suspend fun getCityName(
            latitude: Double,
            longitude: Double,
        ): String =
            withContext(Dispatchers.IO) {
                try {
                    var cityName = "Unknown"
                    cityRepository.getCity(latitude, longitude).collect { response ->

                        if (response is Response.Success) {
                            cityName = response.data?.firstOrNull()?.name ?: "Unknown"
                        }
                    }
                    cityName
                } catch (e: Exception) {
                    "Unknown"
                }
            }

        private suspend fun fetchWeatherFromApi(
            latitude: Double,
            longitude: Double,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
            cityName: String,
        ): WeatherModel =
            weatherService
                .getWeather(latitude, longitude, temperatureUnit, windSpeedUnit, timezone)
                .copy(city = cityName)

        private suspend fun clearOldWeatherData() {
            weatherDao.deleteAllWeather()
            weatherDao.deleteAllCurrentWeather()
            weatherDao.deleteAllCurrentUnits()
            weatherDao.deleteAllHourlyWeather()
            weatherDao.deleteAllHourlyUnits()
        }

        private suspend fun saveWeatherData(weatherModel: WeatherModel) {
            val weatherId = weatherDao.insertWeather(weatherModel.toEntity()).toInt()
            weatherModel.current?.let {
                weatherDao.insertCurrentWeather(weatherModel.toCurrentEntity(weatherId)!!)
            }
            weatherModel.current_units?.let {
                weatherDao.insertCurrentUnits(weatherModel.toCurrentUnitsEntity(weatherId)!!)
            }
            weatherModel.hourly?.let {
                weatherDao.insertHourlyWeather(weatherModel.toHourlyEntity(weatherId)!!)
            }
            weatherModel.hourly_units?.let {
                weatherDao.insertHourlyUnits(weatherModel.toHourlyUnitsEntity(weatherId)!!)
            }
        }

        private suspend fun FlowCollector<Response<WeatherModel>>.handleException(e: Exception) {
            when (e) {
                is SocketTimeoutException ->
                    emit(Response.Failure(CustomError.ApiError(ErrorCode.API_TIMEOUT.message)))
                is HttpException ->
                    emit(Response.Failure(CustomError.ApiError(ErrorCode.NETWORK_ERROR.message)))
                is UnknownHostException, is IOException ->
                    emit(Response.Failure(CustomError.ApiError(ErrorCode.NETWORK_ERROR.message)))
                is CustomError ->
                    emit(Response.Failure(e))
                else ->
                    emit(Response.Failure(CustomError.ApiError(ErrorCode.API_ERROR.message)))
            }
        }

        private suspend fun FlowCollector<Response<WeatherModel>>.emitCachedWeatherIfAvailable() {
            val cachedWeather = getCachedWeather()
            if (cachedWeather != null) {
                emit(Response.Success(cachedWeather.apply { this.cashed = true }))
            }
        }

        private suspend fun getCachedWeather(): WeatherModel? = weatherDao.getAllWeather().firstOrNull()?.toWeatherModel()

        @SuppressLint("MissingPermission")
        private suspend fun getCurrentLocation(): Location? =
            withContext(Dispatchers.IO) {
                val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                try {
                    val locationResult =
                        locationProviderClient.getCurrentLocation(
                            accuracy,
                            CancellationTokenSource().token,
                        )
                    Tasks.await(locationResult)
                    locationResult.result
                } catch (e: Exception) {
                    throw CustomError.LocationUnavailable
                }
            }
    }
