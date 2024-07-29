package com.example.theweatherapp.network.repository

import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.network.service.CityService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    }
