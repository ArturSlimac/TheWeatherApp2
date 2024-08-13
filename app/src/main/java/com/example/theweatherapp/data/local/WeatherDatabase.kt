package com.example.theweatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theweatherapp.domain.model.city.CityItemEntity
import com.example.theweatherapp.domain.model.weather.CurrentEntity
import com.example.theweatherapp.domain.model.weather.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.weather.HourlyEntity
import com.example.theweatherapp.domain.model.weather.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.weather.WeatherEntity
import com.example.theweatherapp.utils.Converters

/**
 * The main Room database for the application, storing weather data and related entities.
 *
 * This database contains tables for weather data, including current weather, hourly forecasts,
 * their associated units, and city information. It also includes type converters to handle
 * custom data types.
 *
 * @property version The version of the database.
 * @property entities The entities that define the tables within the database.
 */
@Database(
    entities = [
        WeatherEntity::class,
        HourlyEntity::class,
        HourlyUnitsEntity::class,
        CurrentEntity::class,
        CurrentUnitsEntity::class,
        CityItemEntity::class,
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    /**
     * Provides access to the [WeatherDao] for performing database operations related to weather data.
     *
     * @return The [WeatherDao] instance.
     */
    abstract fun weatherDao(): WeatherDao

    /**
     * Provides access to the [CityDao] for performing database operations related to city data.
     *
     * @return The [CityDao] instance.
     */
    abstract fun cityDao(): CityDao
}
