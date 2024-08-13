package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

/**
 * A repository interface for managing city-related data.
 * This interface defines methods for retrieving city data based on coordinates or name,
 * as well as managing saved city data in the local database.
 */
interface CityRepository {
    /**
     * Retrieves city information based on geographic coordinates.
     *
     * @param latitude The latitude of the desired location.
     * @param longitude The longitude of the desired location.
     * @return A [Flow] emitting a [Response] containing a [CityModel] with the city information.
     */
    fun getCityByCoordinates(
        latitude: Double,
        longitude: Double,
    ): Flow<Response<CityModel>>

    /**
     * Retrieves a list of cities that match the provided name.
     *
     * @param name The name of the city to search for.
     * @return A [Flow] emitting a [Response] containing a [CityModel] with the matching cities.
     */
    fun getCitiesByName(name: String): Flow<Response<CityModel>>

    /**
     * Retrieves all saved cities from the local database.
     *
     * @return A [Flow] emitting a [Response] containing a [CityModel] with all saved cities.
     */
    fun getAllSavedCities(): Flow<Response<CityModel>>

    /**
     * Deletes a saved city from the local database.
     *
     * @param city The [CityItemModel] representing the city to be deleted.
     */
    suspend fun deleteCity(city: CityItemModel)

    /**
     * Checks if a city is already saved in the local database.
     *
     * @param city The [CityItemModel] representing the city to be checked.
     * @return `true` if the city is saved, `false` otherwise.
     */
    suspend fun isCitySaved(city: CityItemModel): Boolean
}
