package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double?,
    val longitude: Double?,
    val city: String?,
    val timezone: String?,
    val timezoneAbbreviation: String?,
    val utcOffsetSeconds: Int?,
    val elevation: Int?,
    val generationTimeMs: Double?,
)
