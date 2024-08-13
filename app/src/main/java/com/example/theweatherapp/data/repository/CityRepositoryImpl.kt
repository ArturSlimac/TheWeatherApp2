package com.example.theweatherapp.data.repository

import com.example.theweatherapp.data.local.CityDao
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.mappers.toCityItemModel
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.network.service.CityService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Implementation of [CityRepository] that handles city-related data operations.
 * This class communicates with a remote API service and a local database (DAO) to fetch and manage city data.
 *
 * @property cityService The [CityService] used for making network requests to retrieve city data.
 * @property cityDao The [CityDao] used for interacting with the local database.
 * @property apiKey The API key required for making requests to the city API.
 */
class CityRepositoryImpl
    @Inject
    constructor(
        private val cityService: CityService,
        private val cityDao: CityDao,
        private val apiKey: String,
    ) : CityRepository {
        /**
         * @see CityRepository.getCityByCoordinates
         */
        override fun getCityByCoordinates(
            latitude: Double,
            longitude: Double,
        ): Flow<Response<CityModel>> =
            flow {
                emit(Response.Loading)
                try {
                    val city = cityService.getCityByCoordinates(latitude, longitude, apiKey)

                    emit(Response.Success(city))
                } catch (e: Exception) {
                    val errorResponse =
                        when (e) {
                            is SocketTimeoutException -> CustomError.ApiError(ErrorCode.API_TIMEOUT.message)
                            is HttpException -> CustomError.ApiError(ErrorCode.NETWORK_ERROR.message)
                            is UnknownHostException, is IOException -> CustomError.ApiError(ErrorCode.NETWORK_ERROR.message)
                            is CustomError -> e
                            else -> CustomError.ApiError(ErrorCode.API_ERROR.message)
                        }
                    emit(Response.Failure(errorResponse))
                }
            }

        /**
         * @see CityRepository.getCitiesByName
         */
        override fun getCitiesByName(name: String): Flow<Response<CityModel>> =
            flow {
                try {
                    emit(Response.Loading)
                    val cities = cityService.getCitiesByName(name, apiKey)
                    emit(Response.Success(cities))
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException ->
                            emit(Response.Failure(CustomError.ApiError(ErrorCode.API_TIMEOUT.message)))
                        is HttpException ->
                            emit(Response.Failure(CustomError.ApiError(ErrorCode.NETWORK_ERROR.message)))
                        is UnknownHostException, is IOException ->
                            emit(Response.Failure(CustomError.ApiError(ErrorCode.NETWORK_ERROR.message)))
                        is CustomError ->
                            emit(Response.Failure(e))
                        else ->
                            emit(Response.Failure(CustomError.ApiError(ErrorCode.API_ERROR.message)))
                    }
                }
            }

        /**
         * @see CityRepository.getAllSavedCities
         */
        override fun getAllSavedCities(): Flow<Response<CityModel>> =
            flow {
                try {
                    emit(Response.Loading)
                    val cityEntities = cityDao.getAllCities().first()
                    val cityModel =
                        CityModel().apply {
                            addAll(
                                cityEntities.map { it.toCityItemModel() },
                            )
                        }

                    emit(Response.Success(cityModel))
                } catch (e: Exception) {
                    emit(Response.Failure(e))
                }
            }

        /**
         * @see CityRepository.deleteCity
         */
        override suspend fun deleteCity(city: CityItemModel) {
            cityDao.deleteCity(city.name, city.country)
        }

        /**
         * @see CityRepository.isCitySaved
         */
        override suspend fun isCitySaved(city: CityItemModel): Boolean = cityDao.isCitySaved(city.name, city.country)
    }
