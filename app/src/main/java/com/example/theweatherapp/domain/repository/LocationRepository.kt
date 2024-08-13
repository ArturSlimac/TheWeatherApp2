package com.example.theweatherapp.domain.repository

import android.location.Location

/**
 * Interface for a repository that provides location data based on latitude and longitude coordinates.
 */
interface LocationRepository {
    /**
     * Retrieves a [Location] based on the given latitude and longitude or if latitude and longitude tries to get current location from the device.
     *
     * This function is a suspend function, meaning it is designed to be called from a coroutine or
     * another suspend function. It may perform a long-running operation.
     *
     * @param latitude The latitude of the desired location. Can be `null` if latitude is not available.
     * @param longitude The longitude of the desired location. Can be `null` if longitude is not available.
     * @return A [Location] object representing the geographical location based on the provided coordinates.
     * @throws [CustomError.LocationUnavailable] if the location could not be determined based on the provided coordinates.
     */
    suspend fun getLocation(
        latitude: Double?,
        longitude: Double?,
    ): Location
}
