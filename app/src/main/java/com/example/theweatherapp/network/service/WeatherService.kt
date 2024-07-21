package com.example.theweatherapp.network.service

import com.example.theweatherapp.domain.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(
        "v1/forecast?current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,pressure_msl,wind_speed_10m&hourly=temperature_2m,weather_code&forecast_days=1",
    )
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: String,
        @Query("wind_speed_unit") windSpeedUnit: String,
        @Query("timezone") timezone: String,
    ): WeatherModel
}
