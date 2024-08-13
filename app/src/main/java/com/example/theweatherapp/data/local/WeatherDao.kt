package com.example.theweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.theweatherapp.domain.model.WeatherCityAllTogether
import com.example.theweatherapp.domain.model.weather.CurrentEntity
import com.example.theweatherapp.domain.model.weather.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.weather.HourlyEntity
import com.example.theweatherapp.domain.model.weather.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing weather-related operations in the local database.
 *
 * This interface provides methods for inserting and querying weather data,
 * including current weather, hourly weather, and their associated units.
 */
@Dao
interface WeatherDao {
    /**
     * Retrieves the cached weather data, including associated details, for a specific city.
     *
     * This method performs a database transaction to ensure the consistency of the data retrieved.
     *
     * @param name The name of the city for which to retrieve the weather data.
     * @param country The country of the city for which to retrieve the weather data.
     * @return A [Flow] emitting a [WeatherCityAllTogether] object containing the weather data and its details,
     * or `null` if no data is found for the specified city.
     */
    @Transaction
    @Query("SELECT * FROM weather WHERE cityId IN (SELECT id FROM city WHERE name = :name AND country = :country)")
    fun getCachedWeather(
        name: String,
        country: String,
    ): Flow<WeatherCityAllTogether?>

    /**
     * Inserts the main weather data into the database.
     *
     * If the weather data already exists (based on the primary key), it will be replaced.
     *
     * @param weather The [WeatherEntity] object representing the weather data to be inserted.
     * @return The row ID of the inserted weather data.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity): Long

    /**
     * Inserts the current weather data into the database.
     *
     * If the current weather data already exists (based on the primary key), it will be replaced.
     *
     * @param currentWeather The [CurrentEntity] object representing the current weather data to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentEntity)

    /**
     * Inserts the units associated with the current weather data into the database.
     *
     * If the units data already exists (based on the primary key), it will be replaced.
     *
     * @param currentUnits The [CurrentUnitsEntity] object representing the units for the current weather to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUnits(currentUnits: CurrentUnitsEntity)

    /**
     * Inserts the hourly weather data into the database.
     *
     * If the hourly weather data already exists (based on the primary key), it will be replaced.
     *
     * @param hourlyWeather The [HourlyEntity] object representing the hourly weather data to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeather: HourlyEntity)

    /**
     * Inserts the units associated with the hourly weather data into the database.
     *
     * If the units data already exists (based on the primary key), it will be replaced.
     *
     * @param hourlyUnits The [HourlyUnitsEntity] object representing the units for the hourly weather to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyUnits(hourlyUnits: HourlyUnitsEntity)
}
