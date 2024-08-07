package com.example.theweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.theweatherapp.domain.model.WeatherWithDetails
import com.example.theweatherapp.domain.model.weather.CurrentEntity
import com.example.theweatherapp.domain.model.weather.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.weather.HourlyEntity
import com.example.theweatherapp.domain.model.weather.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Transaction
    @Query("SELECT * FROM weather WHERE cityId IN (SELECT id FROM city WHERE name = :name AND country = :country)")
    fun getCachedWeather(
        name: String,
        country: String,
    ): Flow<WeatherWithDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUnits(currentUnits: CurrentUnitsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeather: HourlyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyUnits(hourlyUnits: HourlyUnitsEntity)
}
