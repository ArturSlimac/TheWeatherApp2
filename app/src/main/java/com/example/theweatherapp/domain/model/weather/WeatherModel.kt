package com.example.theweatherapp.domain.model.weather

import com.example.theweatherapp.domain.model.city.CityItemModel
import java.util.Date

data class WeatherModel(
    val current: CurrentModel?,
    val current_units: CurrentUnitsModel?,
    val elevation: Int?,
    val generationtime_ms: Double?,
    val hourly: HourlyModel?,
    val hourly_units: HourlyUnitsModel?,
    val latitude: Double?,
    val longitude: Double?,
    val city: CityItemModel?,
    val timezone: String?,
    val timezone_abbreviation: String?,
    val utc_offset_seconds: Int?,
    var cashed: Boolean = false,
    val lastSync: Date?,
)
