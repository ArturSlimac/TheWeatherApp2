package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_units_weather")
data class CurrentUnitsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int,
    val apparent_temperature: String?,
    val interval: String?,
    val pressure_msl: String?,
    val relative_humidity_2m: String?,
    val temperature_2m: String?,
    val time: String?,
    val weather_code: String?,
    val wind_speed_10m: String?,
)
