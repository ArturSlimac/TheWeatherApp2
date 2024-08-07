package com.example.theweatherapp.domain.model.weather

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.theweatherapp.domain.model.city.CityItemEntity

@Entity(
    tableName = "weather",
    foreignKeys = [
        ForeignKey(
            entity = CityItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["cityId"])],
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityId: Int,
    val latitude: Double?,
    val longitude: Double?,
    val timezone: String?,
    val timezoneAbbreviation: String?,
    val utcOffsetSeconds: Int?,
    val elevation: Int?,
    val generationTimeMs: Double?,
    val createdAt: Long = System.currentTimeMillis(),
)
