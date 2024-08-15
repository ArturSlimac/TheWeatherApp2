package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hourly_units_weather",
    foreignKeys = [
        ForeignKey(
            entity = WeatherEntity::class,
            parentColumns = ["id"],
            childColumns = ["weatherId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["weatherId"]),
    ],
)
data class HourlyUnitsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int,
    val temperature_2m: String?,
    val time: String?,
    val weather_code: String?,
    val is_day: String?,
)
