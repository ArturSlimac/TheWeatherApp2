package com.example.theweatherapp.network.repository

import com.example.theweatherapp.domain.model.WeatherModel
import com.example.theweatherapp.domain.model.toCurrentEntity
import com.example.theweatherapp.domain.model.toCurrentUnitsEntity
import com.example.theweatherapp.domain.model.toEntity
import com.example.theweatherapp.domain.model.toHourlyEntity
import com.example.theweatherapp.domain.model.toHourlyUnitsEntity
import com.example.theweatherapp.domain.model.toWeatherModel
import com.example.theweatherapp.domain.repository.WeatherDao
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl
    @Inject
    constructor(
        private val weatherService: WeatherService,
        private val weatherDao: WeatherDao,
    ) : WeatherRepository {
        override fun getWeather(
            latitude: Double,
            longitude: Double,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
        ): Flow<Response<WeatherModel>> =
            flow {
                emit(Response.Loading)

                val cachedWeather = weatherDao.getWeather(latitude, longitude).firstOrNull()
                if (cachedWeather != null) {
                    emit(Response.Success(cachedWeather.toWeatherModel()))
                }
                try {
                    val responseApi =
                        weatherService.getWeather(
                            latitude,
                            longitude,
                            temperatureUnit,
                            windSpeedUnit,
                            timezone,
                        )
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
                    if (cachedWeather != null) {
                        emit(Response.Success(cachedWeather.toWeatherModel()))
                    } else {
                        emit(Response.Failure(e))
                    }
                }
            }.flowOn(Dispatchers.IO)
    }
