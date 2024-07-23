package com.example.theweatherapp.domain.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theweatherapp.domain.model.CurrentEntity
import com.example.theweatherapp.domain.model.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.HourlyEntity
import com.example.theweatherapp.domain.model.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.WeatherEntity
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
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
