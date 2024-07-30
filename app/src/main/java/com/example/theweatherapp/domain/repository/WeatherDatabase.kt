package com.example.theweatherapp.domain.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theweatherapp.domain.model.weather.CurrentEntity
import com.example.theweatherapp.domain.model.weather.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.weather.HourlyEntity
import com.example.theweatherapp.domain.model.weather.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.weather.WeatherEntity
import com.example.theweatherapp.utils.Converters

@Database(
    entities = [
        WeatherEntity::class,
        HourlyEntity::class,
        HourlyUnitsEntity::class,
        CurrentEntity::class,
        CurrentUnitsEntity::class,
    ],
    version = 1,
    // autoMigrations = [
    //   AutoMigration(from = 2, to = 3),
    // ],
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
