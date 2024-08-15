package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hourly_weather",
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
data class HourlyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int?,
    val temperature_2m: List<Double>?,
    val time: List<String>?,
    val weather_code: List<Int>?,
    val is_day: List<Int>?,
)
