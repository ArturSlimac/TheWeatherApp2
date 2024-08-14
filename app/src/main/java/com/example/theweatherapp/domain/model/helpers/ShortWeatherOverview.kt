package com.example.theweatherapp.domain.model.helpers

/**
 * Data class representing a brief overview of weather information for a specific location.
 *
 * @property cityName The name of the city or location for which the weather overview is provided.
 * @property temperature2m The current temperature at 2 meters above the ground, represented as a [Pair] of the temperature value (in degrees) and the unit (e.g., "°C" or "°F").
 * @property apparentTemperature The "feels like" temperature, represented as a [Pair] of the apparent temperature value (in degrees) and the unit (e.g., "°C" or "°F").
 *
 */
data class ShortWeatherOverview(
    val cityName: String,
    val temperature2m: Pair<Int, String>,
    val apparentTemperature: Pair<Int, String>,
)
