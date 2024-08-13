package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing weather data.
 *
 * This interface provides methods for fetching weather data from a remote source or local cache,
 * and for saving weather data to the local database. Implementations of this interface
 * are responsible for the logic related to data retrieval and storage.
 */
interface WeatherRepository {
/**
     * Retrieves weather data for a specific city or current location.
     *
     * This method fetches weather information either from a remote API or from local cache,
     * depending on the availability and the provided parameters. If the city is not provided,
     * it will attempt to use the current location to fetch weather data.
     *
     * @param city Optional [CityItemModel] representing the city for which to fetch weather data.
     *             If not provided, the current location will be used.
     * @param temperatureUnit The unit of temperature to be used (e.g., Celsius or Fahrenheit).
     * @param windSpeedUnit The unit of wind speed to be used (e.g., meters per second or kilometers per hour).
     * @param timezone The timezone to be used for the weather data.
     * @return A [Flow] emitting a [Response] object containing the [WeatherModel] with weather data
     *         or an error response if the data cannot be retrieved.
     *
     * */
    fun getWeather(
        city: CityItemModel? = null,
        temperatureUnit: String,
        windSpeedUnit: String,
        timezone: String,
    ): Flow<Response<WeatherModel>>

    /**
     * Saves weather data to the local database.
     *
     * This method stores the provided [WeatherModel] into the local database, including
     * all relevant details such as current weather, current units, and hourly weather.
     *
     * @param weatherModel The [WeatherModel] containing weather data to be saved.
     *
     */
    suspend fun saveWeather(weatherModel: WeatherModel)
}
