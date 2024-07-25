package com.example.theweatherapp.domain.model.weather

data class WeatherModel(
    val current: Current?,
    val current_units: CurrentUnits?,
    val elevation: Int?,
    val generationtime_ms: Double?,
    val hourly: Hourly?,
    val hourly_units: HourlyUnits?,
    val latitude: Double?,
    val longitude: Double?,
    val city: String?,
    val timezone: String?,
    val timezone_abbreviation: String?,
    val utc_offset_seconds: Int?,
    var cashed: Boolean = false,
)
