package com.example.theweatherapp.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.theweatherapp.domain.model.city.CityItemEntity
import com.example.theweatherapp.domain.model.weather.CurrentEntity
import com.example.theweatherapp.domain.model.weather.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.weather.HourlyEntity
import com.example.theweatherapp.domain.model.weather.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.weather.WeatherEntity

data class WeatherCityAllTogether(
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
    @Relation(
        parentColumn = "cityId",
        entityColumn = "id",
    )
    val city: CityItemEntity?,
)
