package com.example.theweatherapp.domain.model.city

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "city",
    indices = [Index(value = ["country", "name"], unique = true)],
)
data class CityItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val country: String,
    val name: String,
    val state: String,
    val latitude: Double?,
    val longitude: Double?,
)
