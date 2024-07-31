package com.example.theweatherapp.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getLocation(
        latitude: Double?,
        longitude: Double?,
    ): Location
}
