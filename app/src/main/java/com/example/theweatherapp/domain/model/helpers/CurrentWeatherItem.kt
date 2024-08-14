package com.example.theweatherapp.domain.model.helpers

/**
 * A data model representing the current weather details.
 *
 * @property pressure A [Pair] containing the pressure value and its unit (e.g., 1013 hPa).
 * @property humidity A [Pair] containing the humidity value and its unit (e.g., 60%).
 * @property windSpeed A [Pair] containing the wind speed value and its unit (e.g., 15 km/h).
 * @property weatherType An [WeatherType] object representing the type of weather (e.g., sunny, rainy), including associated icons and titles.
 */
data class CurrentWeatherItem(
    val pressure: Pair<Double, String>,
    val humidity: Pair<Double, String>,
    val windSpeed: Pair<Double, String>,
    val weatherType: WeatherType,
)
