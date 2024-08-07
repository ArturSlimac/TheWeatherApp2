package com.example.theweatherapp.domain.model.helpers

data class ShortWeatherOverview(
    val cityName: String,
    val temperature2m: Pair<Int, String>,
    val apparentTemperature: Pair<Int, String>,
)
