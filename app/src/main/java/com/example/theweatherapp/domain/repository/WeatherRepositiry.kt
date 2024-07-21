package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.domain.model.WeatherModel
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        latitude: Double,
        longitude: Double,
        temperatureUnit: String,
        windSpeedUnit: String,
        timezone: String,
    ): Flow<Response<WeatherModel>>
}
