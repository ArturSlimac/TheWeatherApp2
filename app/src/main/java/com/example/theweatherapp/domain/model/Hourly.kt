package com.example.theweatherapp.domain.model

data class Hourly(
    val temperature_2m: List<Double> = listOf(),
    val time: List<String> = listOf(),
    val weather_code: List<Int> = listOf(),
)
