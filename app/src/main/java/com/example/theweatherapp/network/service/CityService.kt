package com.example.theweatherapp.network.service

import com.example.theweatherapp.domain.model.city.CityModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityService {
    @GET("v1/reversegeocoding")
    suspend fun getCity(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Header("X-Api-Key") apiKey: String,
    ): CityModel

    @GET("v1/geocoding")
    suspend fun getCitiesByName(
        @Query("city") name: String,
        @Header("X-Api-Key") apiKey: String,
    ): CityModel
}
