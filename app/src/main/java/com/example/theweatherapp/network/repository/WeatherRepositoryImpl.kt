package com.example.theweatherapp.network.repository

import android.annotation.SuppressLint
import android.location.Location
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
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

                val location =
                    if (latitude == null || longitude == null) {
                        getCurrentLocation()
                    } else {
                        Location("").apply {
                            this.latitude = latitude
                            this.longitude = longitude
                        }
                    }

                if (location == null) {
                    val cachedWeather = getCachedWeather()
                    if (cachedWeather != null) {
                        emit(
                            Response.Success(
                                cachedWeather.apply {
                                    this.cashed = true
                                },
                            ),
                        )
                    } else {
                        emit(Response.Failure(Exception("Location not available and no cached data")))
                    }
                } else {
                    val cityName = getCityName(location.latitude, location.longitude)

                    try {
                        val responseApi =
                            weatherService
                                .getWeather(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    temperatureUnit = temperatureUnit,
                                    windSpeedUnit = windSpeedUnit,
                                    timezone = timezone,
                                ).copy(city = cityName)

                        // Delete previous data
                        weatherDao.deleteAllWeather()
                        weatherDao.deleteAllCurrentWeather()
                        weatherDao.deleteAllCurrentUnits()
                        weatherDao.deleteAllHourlyWeather()
                        weatherDao.deleteAllHourlyUnits()

                        val weatherId = weatherDao.insertWeather(responseApi.toEntity()).toInt()
                        responseApi.current?.let {
                            weatherDao.insertCurrentWeather(responseApi.toCurrentEntity(weatherId)!!)
                        }

                        responseApi.current_units?.let {
                            weatherDao.insertCurrentUnits(responseApi.toCurrentUnitsEntity(weatherId)!!)
                        }

                        responseApi.hourly?.let {
                            weatherDao.insertHourlyWeather(responseApi.toHourlyEntity(weatherId)!!)
                        }

                        responseApi.hourly_units?.let {
                            weatherDao.insertHourlyUnits(responseApi.toHourlyUnitsEntity(weatherId)!!)
                        }
                        emit(Response.Success(responseApi))
                    } catch (e: Exception) {
                        val cachedWeather = getCachedWeather()
                        if (cachedWeather != null) {
                            emit(
                                Response.Success(
                                    cachedWeather.apply {
                                        this.cashed = true
                                    },
                                ),
                            )
                        } else {
                            emit(Response.Failure(e))
                        }
                    }
                }
            }.flowOn(Dispatchers.IO)

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
                    null
                }
            }
    }
