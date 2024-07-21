package com.example.theweatherapp.domain.model

data class CurrentUnits(
    val apparent_temperature: String? = null,
    val interval: String? = null,
    val pressure_msl: String? = null,
    val relative_humidity_2m: String? = null,
    val temperature_2m: String? = null,
    val time: String? = null,
    val weather_code: String? = null,
    val wind_speed_10m: String? = null,
)
