package com.example.theweatherapp.domain.repository

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
    @Query("SELECT * FROM weather WHERE latitude = :latitude AND longitude = :longitude")
    fun getWeather(
        latitude: Double,
        longitude: Double,
    ): Flow<WeatherWithDetails?>

    @Transaction
    @Query("SELECT * FROM weather LIMIT 1")
    fun getAllWeather(): Flow<WeatherWithDetails?>

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

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()

    @Query("DELETE FROM current_weather")
    suspend fun deleteAllCurrentWeather()

    @Query("DELETE FROM current_units_weather")
    suspend fun deleteAllCurrentUnits()

    @Query("DELETE FROM hourly_weather")
    suspend fun deleteAllHourlyWeather()

    @Query("DELETE FROM hourly_units_weather")
    suspend fun deleteAllHourlyUnits()
}
