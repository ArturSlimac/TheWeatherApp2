package com.example.theweatherapp.network.service

import com.example.theweatherapp.domain.model.city.CityModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Service interface for interacting with the city-related API endpoints.
 *
 * This interface provides methods to retrieve city information based on coordinates or name.
 */
interface CityService {
    /**
     * Retrieves city information based on the provided latitude and longitude coordinates.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param apiKey The API key for authentication.
     * @return A [CityModel] containing city information corresponding to the provided coordinates.
     */
    @GET("v1/reversegeocoding")
    suspend fun getCityByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Header("X-Api-Key") apiKey: String,
    ): CityModel

    /**
     * Retrieves a list of cities matching the provided name.
     *
     * @param name The name of the city to search for.
     * @param apiKey The API key for authentication.
     * @return A [CityModel] containing a list of cities that match the provided name.
     */
    @GET("v1/geocoding")
    suspend fun getCitiesByName(
        @Query("city") name: String,
        @Header("X-Api-Key") apiKey: String,
    ): CityModel
}
