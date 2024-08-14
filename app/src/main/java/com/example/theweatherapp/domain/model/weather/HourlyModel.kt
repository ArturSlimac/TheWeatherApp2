package com.example.theweatherapp.domain.model.weather

data class HourlyModel(
    val temperature_2m: List<Double>?,
    val time: List<String>?,
    val weather_code: List<Int>?,
    val is_day: List<Int>?,
)
