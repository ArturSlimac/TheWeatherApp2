package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int,
    val apparent_temperature: Double?,
    val interval: Int?,
    val pressure_msl: Double?,
    val relative_humidity_2m: Double?,
    val temperature_2m: Double?,
    val time: String?,
    val weather_code: Int?,
    val wind_speed_10m: Double?,
)
