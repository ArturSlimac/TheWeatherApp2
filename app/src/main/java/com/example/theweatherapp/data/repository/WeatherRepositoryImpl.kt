package com.example.theweatherapp.data.repository

import com.example.theweatherapp.data.local.CityDao
import com.example.theweatherapp.data.local.WeatherDao
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.domain.mappers.toCityItemEntity
import com.example.theweatherapp.domain.mappers.toCurrentEntity
import com.example.theweatherapp.domain.mappers.toCurrentUnitsEntity
import com.example.theweatherapp.domain.mappers.toEntity
import com.example.theweatherapp.domain.mappers.toHourlyEntity
import com.example.theweatherapp.domain.mappers.toHourlyUnitsEntity
import com.example.theweatherapp.domain.mappers.toWeatherModel
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.LocationRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the [WeatherRepository] interface that interacts with the network and local database
 * to manage weather data.
 *
 * @param weatherService The service interface for fetching weather data from the API.
 * @param weatherDao DAO for accessing and manipulating weather data in the local database.
 * @param cityDao DAO for accessing and manipulating city data in the local database.
 * @param cityRepository Repository for fetching city data.
 * @param locationRepository Repository for accessing location data.
 */
@Singleton
class WeatherRepositoryImpl
    @Inject
    constructor(
        private val weatherService: WeatherService,
        private val weatherDao: WeatherDao,
        private val cityDao: CityDao,
        private val cityRepository: CityRepository,
        private val locationRepository: LocationRepository,
    ) : WeatherRepository {
        /**
         * @see WeatherRepository.getWeather
         */
        override fun getWeather(
            city: CityItemModel?,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
        ): Flow<Response<WeatherModel>> =
            flow {
                emit(Response.Loading)
                try {
                    val location = locationRepository.getLocation(city?.latitude, city?.longitude)
                    val gottenCity =
                        city ?: getCity(location.latitude, location.longitude)

                    val responseApi =
                        fetchWeatherFromApi(
                            location.latitude,
                            location.longitude,
                            temperatureUnit,
                            windSpeedUnit,
                            timezone,
                            gottenCity,
                        )

                    emit(Response.Success(responseApi))
                } catch (e: Exception) {
                    handleException(e)
                    emitCachedWeatherIfAvailable(city)
                }
            }.flowOn(Dispatchers.IO)

        /**
         * @see WeatherRepository.saveWeather
         */
        override suspend fun saveWeather(weatherModel: WeatherModel) {
            val cityId = cityDao.insertCityItem(weatherModel.toCityItemEntity()!!).toInt()

            val weatherId = weatherDao.insertWeather(weatherModel.toEntity(cityId)).toInt()

            weatherModel.current?.let {
                weatherDao.insertCurrentWeather(weatherModel.toCurrentEntity(weatherId)!!)
            }
            weatherModel.current_units?.let {
                weatherDao.insertCurrentUnits(weatherModel.toCurrentUnitsEntity(weatherId)!!)
            }
            weatherModel.hourly?.let {
                weatherDao.insertHourlyWeather(weatherModel.toHourlyEntity(weatherId)!!)
            }
            weatherModel.hourly_units?.let {
                weatherDao.insertHourlyUnits(weatherModel.toHourlyUnitsEntity(weatherId)!!)
            }
        }

        /**
         * Retrieves the city information based on the provided latitude and longitude.
         *
         * This method tries to find city information using the [CityRepository] based on the
         * provided coordinates.
         *
         * @param latitude The latitude of the location.
         * @param longitude The longitude of the location.
         * @return A [CityItemModel] representing the city information.
         *
         * @throws CustomError.CityNotFound If the city information could not be found.
         */
        private suspend fun getCity(
            latitude: Double,
            longitude: Double,
        ): CityItemModel =
            withContext(Dispatchers.IO) {
                try {
                    var cityItem =
                        CityItemModel(
                            latitude = latitude,
                            longitude = longitude,
                        )
                    cityRepository
                        .getCityByCoordinates(latitude, longitude)
                        .collect { response ->
                            if (response is Response.Success) {
                                cityItem = response.data?.firstOrNull() ?: cityItem
                            }
                        }
                    cityItem
                } catch (e: Exception) {
                    throw CustomError.CityNotFound
                }
            }

        /**
         * Fetches weather data from the API.
         *
         * @param latitude The latitude of the location.
         * @param longitude The longitude of the location.
         * @param temperatureUnit The unit of temperature to be used.
         * @param windSpeedUnit The unit of wind speed to be used.
         * @param timezone The timezone to be used.
         * @param city The [CityItemModel] to associate with the weather data.
         * @return A [WeatherModel] containing the fetched weather data.
         */
        private suspend fun fetchWeatherFromApi(
            latitude: Double,
            longitude: Double,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
            city: CityItemModel,
        ): WeatherModel =
            weatherService
                .getWeather(latitude, longitude, temperatureUnit, windSpeedUnit, timezone)
                .copy(city = city)

        /**
         * Handles exceptions that occur during API calls.
         *
         * This method emits appropriate error responses based on the exception type.
         *
         * @param e The [Exception] that was thrown.
         */
        private suspend fun FlowCollector<Response<WeatherModel>>.handleException(e: Exception) {
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

        /**
         * Emits cached weather data if available.
         *
         * This method checks if there is cached weather data for the given city and emits it
         * if it exists.
         *
         * @param city The [CityItemModel] for which to fetch cached weather data.
         */
        private suspend fun FlowCollector<Response<WeatherModel>>.emitCachedWeatherIfAvailable(city: CityItemModel?) {
            if (city == null) {
                return
            }
            val cachedWeather = getCachedWeather(city)
            if (cachedWeather != null) {
                emit(Response.Success(cachedWeather.apply { this.cashed = true }))
            }
        }

        /**
         * Retrieves cached weather data for the given city.
         *
         * @param city The [CityItemModel] for which to fetch cached weather data.
         * @return A [WeatherModel] containing the cached weather data, or `null` if not found.
         */
        private suspend fun getCachedWeather(city: CityItemModel): WeatherModel? =
            weatherDao.getCachedWeather(city.name, city.country).firstOrNull()?.toWeatherModel()
    }
