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
}
