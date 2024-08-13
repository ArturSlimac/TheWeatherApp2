package com.example.theweatherapp.domain.model.weather

import com.example.theweatherapp.domain.model.city.CityItemModel
import java.util.Date

data class WeatherModel(
    val current: CurrentModel?,
    val current_units: CurrentUnitsModel?,
    val hourly: HourlyModel?,
    val hourly_units: HourlyUnitsModel?,
    val city: CityItemModel?,
    val timezone: String?,
    val timezone_abbreviation: String?,
    var cashed: Boolean = false,
    val lastSync: Date?,
)
