package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        latitude: Double? = null,
        longitude: Double? = null,
        temperatureUnit: String,
        windSpeedUnit: String,
        timezone: String,
    ): Flow<Response<WeatherModel>>
}
