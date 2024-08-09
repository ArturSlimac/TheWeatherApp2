package com.example.theweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.theweatherapp.domain.model.city.CityItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityItem(cityItem: CityItemEntity): Long

    @Query("SELECT * FROM city")
    fun getAllCities(): Flow<List<CityItemEntity>>

    @Query("DELETE FROM city WHERE name = :name AND country = :country")
    suspend fun deleteCity(
        name: String,
        country: String,
    )

    @Query("SELECT EXISTS(SELECT 1 FROM city WHERE name = :name AND country = :country LIMIT 1)")
    fun isCitySaved(
        name: String,
        country: String,
    ): Boolean
}
