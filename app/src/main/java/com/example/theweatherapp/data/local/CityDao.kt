package com.example.theweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.theweatherapp.domain.model.city.CityItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing city-related operations in the local database.
 *
 * This interface provides methods for inserting, querying, and deleting city items.
 */
@Dao
interface CityDao {
    /**
     * Inserts a [CityItemEntity] into the database.
     *
     * If the city already exists in the database (determined by the primary key), it will be replaced.
     *
     * @param cityItem The city item to be inserted.
     * @return The row ID of the inserted item.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityItem(cityItem: CityItemEntity): Long

    /**
     * Retrieves all cities from the database.
     *
     * @return A [Flow] that emits a list of [CityItemEntity] objects representing all cities in the database.
     */
    @Query("SELECT * FROM city")
    fun getAllCities(): Flow<List<CityItemEntity>>

    /**
     * Deletes a city from the database based on the provided name and country.
     *
     * @param name The name of the city to delete.
     * @param country The country of the city to delete.
     */
    @Query("DELETE FROM city WHERE name = :name AND country = :country")
    suspend fun deleteCity(
        name: String,
        country: String,
    )

    /**
     * Checks if a city exists in the database based on the provided name and country.
     *
     * @param name The name of the city to check.
     * @param country The country of the city to check.
     * @return `true` if the city exists in the database, `false` otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM city WHERE name = :name AND country = :country LIMIT 1)")
    fun isCitySaved(
        name: String,
        country: String,
    ): Boolean
}
