package com.example.theweatherapp.data.repository

import com.example.theweatherapp.data.local.CityDao
import com.example.theweatherapp.data.local.WeatherDao
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.mappers.toCityItemEntity
import com.example.theweatherapp.domain.mappers.toCurrentEntity
import com.example.theweatherapp.domain.mappers.toCurrentUnitsEntity
import com.example.theweatherapp.domain.mappers.toEntity
import com.example.theweatherapp.domain.mappers.toHourlyEntity
import com.example.theweatherapp.domain.mappers.toHourlyUnitsEntity
import com.example.theweatherapp.domain.mappers.toWeatherModel
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.LocationRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Response
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
        private val cityDao: CityDao,
        private val cityRepository: CityRepository,
        private val locationRepository: LocationRepository,
    ) : WeatherRepository {
        override fun getWeather(
            city: CityItemModel?,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
        ): Flow<Response<WeatherModel>> =
            flow {
                emit(Response.Loading)
                try {
                    val location = locationRepository.getLocation(city?.latitude, city?.longitude)
                    val gottenCity =
                        city ?: getCity(location.latitude, location.longitude)

                    val responseApi =
                        fetchWeatherFromApi(
                            location.latitude,
                            location.longitude,
                            temperatureUnit,
                            windSpeedUnit,
                            timezone,
                            gottenCity,
                        )

                    emit(Response.Success(responseApi))
                } catch (e: Exception) {
                    handleException(e)
                    emitCachedWeatherIfAvailable()
                }
            }.flowOn(Dispatchers.IO)

        override suspend fun saveWeather(weatherModel: WeatherModel) {
            val cityId = cityDao.insertCityItem(weatherModel.toCityItemEntity()!!).toInt()

            val weatherId = weatherDao.insertWeather(weatherModel.toEntity(cityId)).toInt()

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

        private suspend fun getCity(
            latitude: Double,
            longitude: Double,
        ): CityItemModel =
            withContext(Dispatchers.IO) {
                try {
                    var cityItem =
                        CityItemModel(
                            country = "Unknown",
                            name = "Unknown",
                            state = "Unknown",
                            latitude = latitude,
                            longitude = longitude,
                        )
                    cityRepository
                        .getCity(latitude, longitude)
                        .collect { response ->
                            if (response is Response.Success) {
                                cityItem = response.data?.firstOrNull() ?: cityItem
                            }
                        }
                    cityItem
                } catch (e: Exception) {
                    throw CustomError.CityNotFound
                }
            }

        private suspend fun fetchWeatherFromApi(
            latitude: Double,
            longitude: Double,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
            city: CityItemModel,
        ): WeatherModel =
            weatherService
                .getWeather(latitude, longitude, temperatureUnit, windSpeedUnit, timezone)
                .copy(city = city)

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
    }
