package com.example.theweatherapp.domain.model

data class WeatherModel(
    val current: Current? = null,
    val current_units: CurrentUnits? = null,
    val elevation: Int? = null,
    val generationtime_ms: Double? = null,
    val hourly: Hourly? = null,
    val hourly_units: HourlyUnits? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezone: String? = null,
    val timezone_abbreviation: String? = null,
    val utc_offset_seconds: Int? = null,
)
