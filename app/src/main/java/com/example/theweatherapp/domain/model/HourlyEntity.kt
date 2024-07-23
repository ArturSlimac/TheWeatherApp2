package com.example.theweatherapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_weather")
data class HourlyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int?,
    val temperature_2m: List<Double>?,
    val time: List<String>?,
    val weather_code: List<Int>?,
)
