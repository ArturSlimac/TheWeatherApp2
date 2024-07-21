package com.example.theweatherapp.domain.model

data class Current(
    val apparent_temperature: Double? = null,
    val interval: Int? = null,
    val pressure_msl: Double? = null,
    val relative_humidity_2m: Double? = null,
    val temperature_2m: Double? = null,
    val time: String? = null,
    val weather_code: Int? = null,
    val wind_speed_10m: Double? = null,
)
