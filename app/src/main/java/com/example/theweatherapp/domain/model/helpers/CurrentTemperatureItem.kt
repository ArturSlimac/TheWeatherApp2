package com.example.theweatherapp.domain.model.helpers

data class CurrentTemperatureItem(
    val temperature2m: Pair<Int, String>,
    val apparentTemperature: Pair<Int, String>,
    val temperatureUiDetails: TemperatureUiDetails,
)
