package com.example.theweatherapp.data.repository

import com.example.theweatherapp.data.local.CityDao
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.mappers.toModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.network.service.CityService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CityRepositoryImpl
    @Inject
    constructor(
        private val cityService: CityService,
        private val cityDao: CityDao,
        private val apiKey: String,
    ) : CityRepository {
        override fun getCity(
            latitude: Double,
            longitude: Double,
        ): Flow<Response<CityModel>> =
            flow {
                emit(Response.Loading)
                try {
                    val city = cityService.getCity(latitude, longitude, apiKey)
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

        override fun getAllSavedCities(): Flow<Response<CityModel>> =
            flow {
                try {
                    emit(Response.Loading)
                    val cityEntities = cityDao.getAllCities().first()
                    val cityModel =
                        CityModel().apply {
                            addAll(
                                cityEntities.map { it.toModel() },
                            )
                        }

                    emit(Response.Success(cityModel))
                } catch (e: Exception) {
                    emit(Response.Failure(e))
                }
            }
    }
