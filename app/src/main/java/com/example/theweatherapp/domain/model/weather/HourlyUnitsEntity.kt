package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_units_weather")
data class HourlyUnitsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int,
    val temperature_2m: String?,
    val time: String?,
    val weather_code: String?,
)
