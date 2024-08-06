package com.example.theweatherapp.domain.model.helpers

data class CurrentWeatherItem(
    val pressure: Pair<Double, String>,
    val humidity: Pair<Double, String>,
    val windSpeed: Pair<Double, String>,
    val weatherType: WeatherType,
)
