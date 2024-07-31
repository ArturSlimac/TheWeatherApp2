package com.example.theweatherapp.domain.model.city

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.theweatherapp.domain.model.weather.WeatherEntity

@Entity(
    tableName = "city",
    foreignKeys = [
        ForeignKey(
            entity = WeatherEntity::class,
            parentColumns = ["id"],
            childColumns = ["weatherId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["country", "name"], unique = true)],
)
data class CityItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weatherId: Int,
    val country: String,
    val name: String,
    val state: String?,
    val latitude: Double?,
    val longitude: Double?,
)
