package com.example.theweatherapp.domain.model.helpers

/**
 * A data model representing the current temperature information and associated UI details.
 *
 * @property temperature2m A [Pair] containing the current temperature value and its unit (e.g., 32°C).
 * @property apparentTemperature A [Pair] representing the "feels like" temperature and its unit.
 * @property temperatureUiDetails An [TemperatureUiDetails] object containing colors information for the gradient and description depending on the temperature.
 */
data class CurrentTemperatureItem(
    val temperature2m: Pair<Int, String>,
    val apparentTemperature: Pair<Int, String>,
    val temperatureUiDetails: TemperatureUiDetails,
)
