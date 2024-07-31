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

@Singleton
class LocationRepositoryImpl
    @Inject
    constructor(
        private val locationProviderClient: FusedLocationProviderClient,
    ) : LocationRepository {
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
