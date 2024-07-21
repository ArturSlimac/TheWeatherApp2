package com.example.theweatherapp.network.repository

import com.example.theweatherapp.domain.model.WeatherModel
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl
    @Inject
    constructor(
        private val weatherService: WeatherService,
    ) : WeatherRepository {
        override fun getWeather(
            latitude: Double,
            longitude: Double,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
        ): Flow<Response<WeatherModel>> =
            flow {
                try {
                    emit(Response.Loading)
                    val responseApi =
                        weatherService.getWeather(
                            latitude,
                            longitude,
                            temperatureUnit,
                            windSpeedUnit,
                            timezone,
                        )
                    emit(Response.Success(responseApi))
                } catch (e: Exception) {
                    emit(Response.Failure(e))
                }
            }.flowOn(Dispatchers.IO)
    }
