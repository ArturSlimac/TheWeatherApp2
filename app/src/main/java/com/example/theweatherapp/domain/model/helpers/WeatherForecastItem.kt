package com.example.theweatherapp.domain.model.helpers

import androidx.annotation.DrawableRes

data class WeatherForecastItem(
    val temperature: Pair<Int, String>,
    val time: Pair<String, String>,
    val isDay: Boolean,
    @DrawableRes val weatherIcon: Int,
)
