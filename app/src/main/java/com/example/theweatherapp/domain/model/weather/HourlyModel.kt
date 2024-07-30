package com.example.theweatherapp.domain.model.weather

import com.example.theweatherapp.domain.model.helpers.WeatherType

data class HourlyModel(
    val temperature_2m: List<Double>?,
    val time: List<String>?,
    val weather_code: List<Int>?,
) {
    fun getWeatherTypes(): List<WeatherType>? = weather_code?.map { WeatherType.fromWmoStandard(it) }
}
