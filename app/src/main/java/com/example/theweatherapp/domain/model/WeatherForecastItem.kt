package com.example.theweatherapp.domain.model

import androidx.annotation.DrawableRes

data class WeatherForecastItem(
    val temperature: Pair<Int, String>,
    val time: Pair<String, String>,
    @DrawableRes val weatherIcon: Int,
)
