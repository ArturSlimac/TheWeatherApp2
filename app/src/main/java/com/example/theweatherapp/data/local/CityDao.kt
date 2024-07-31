package com.example.theweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.theweatherapp.domain.model.city.CityItemEntity

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityItem(cityItem: CityItemEntity)
}
