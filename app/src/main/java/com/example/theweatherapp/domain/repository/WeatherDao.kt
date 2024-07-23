package com.example.theweatherapp.domain.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.theweatherapp.domain.model.CurrentEntity
import com.example.theweatherapp.domain.model.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.HourlyEntity
import com.example.theweatherapp.domain.model.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.WeatherEntity
import com.example.theweatherapp.domain.model.WeatherWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Transaction
    @Query("SELECT * FROM weather WHERE latitude = :latitude AND longitude = :longitude")
    fun getWeather(
        latitude: Double,
        longitude: Double,
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
