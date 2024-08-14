package com.example.theweatherapp.domain.model.helpers

import androidx.annotation.DrawableRes

/**
 * A data model representing a weather forecast for a specific time.
 *
 * @property temperature A [Pair] containing the temperature value and its unit (e.g., 22°C).
 * @property time A [Pair] containing the time of the forecast and its unit (e.g., "15:00" and "UTC").
 * @property isDay A [Boolean] indicating whether the forecast is for daytime (`true`) or nighttime (`false`).
 * @property weatherIcon A drawable resource ID [DrawableRes] representing the icon associated with the weather condition (e.g., sunny, rainy).
 */
data class WeatherForecastItem(
    val temperature: Pair<Int, String>,
    val time: Pair<String, String>,
    val isDay: Boolean,
    @DrawableRes val weatherIcon: Int,
)
