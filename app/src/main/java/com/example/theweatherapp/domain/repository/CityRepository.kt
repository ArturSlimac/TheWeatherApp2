package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun getCity(
        latitude: Double,
        longitude: Double,
    ): Flow<Response<CityModel>>

    fun getCitiesByName(name: String): Flow<Response<CityModel>>

    fun getAllSavedCities(): Flow<Response<CityModel>>
}
