package com.example.theweatherapp.network.repository

import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.network.service.CityService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CityRepositoryImpl
    @Inject
    constructor(
        private val cityService: CityService,
        private val apiKey: String,
    ) : CityRepository {
        override fun getCity(
            latitude: Double,
            longitude: Double,
        ): Flow<Response<CityModel>> =
            flow {
                try {
                    emit(Response.Loading)
                    val city = cityService.getCity(latitude, longitude, apiKey)
                    emit(Response.Success(city))
                } catch (e: Exception) {
                    emit(Response.Failure(e))
                }
            }
    }
