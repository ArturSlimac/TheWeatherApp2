package com.example.theweatherapp.data.device

import android.annotation.SuppressLint
import android.location.Location
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the [LocationRepository] interface that uses the Fused Location Provider API
 * to retrieve location data. It can either provide the current location of the device or a location
 * based on provided latitude and longitude.
 *
 * This class is marked as a [Singleton] and should be injected using dependency injection.
 *
 * @property locationProviderClient The [FusedLocationProviderClient] used to obtain the device's current location.
 */
@Singleton
class LocationRepositoryImpl
    @Inject
    constructor(
        private val locationProviderClient: FusedLocationProviderClient,
    ) : LocationRepository {
        /**
         * @see LocationRepository.getLocation
         */
        override suspend fun getLocation(
            latitude: Double?,
            longitude: Double?,
        ): Location =
            if (latitude == null || longitude == null) {
                getCurrentLocation()
            } else {
                Location("").apply {
                    this.latitude = latitude
                    this.longitude = longitude
                }
            }

        /**
         * Retrieves the current location of the device using the Fused Location Provider API.
         *
         * This method is called internally when either latitude or longitude is `null` in the [getLocation] method.
         *
         * @return A [Location] object representing the current location of the device.
         * @throws CustomError.LocationUnavailable if the location could not be determined.
         */
        @SuppressLint("MissingPermission")
        private suspend fun getCurrentLocation(): Location =
            withContext(Dispatchers.IO) {
                val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                try {
                    val locationResult =
                        locationProviderClient.getCurrentLocation(
                            accuracy,
                            CancellationTokenSource().token,
                        )
                    Tasks.await(locationResult)
                    locationResult.result
                } catch (e: Exception) {
                    throw CustomError.LocationUnavailable
                }
            }
    }
