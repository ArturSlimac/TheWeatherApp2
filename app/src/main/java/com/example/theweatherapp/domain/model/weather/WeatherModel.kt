package com.example.theweatherapp.domain.model.weather

data class WeatherModel(
    val current: CurrentModel?,
    val current_units: CurrentUnitsModel?,
    val elevation: Int?,
    val generationtime_ms: Double?,
    val hourly: HourlyModel?,
    val hourly_units: HourlyUnitsModel?,
    val latitude: Double?,
    val longitude: Double?,
    val city: String?,
    val timezone: String?,
    val timezone_abbreviation: String?,
    val utc_offset_seconds: Int?,
    var cashed: Boolean = false,
)
