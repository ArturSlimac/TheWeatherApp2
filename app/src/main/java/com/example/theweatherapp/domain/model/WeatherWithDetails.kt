package com.example.theweatherapp.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherWithDetails(
    @Embedded val weather: WeatherEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherId",
    )
    val currentWeather: CurrentEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherId",
    )
    val currentUnits: CurrentUnitsEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherId",
    )
    val hourlyWeather: HourlyEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherId",
    )
    val hourlyUnits: HourlyUnitsEntity?,
)
