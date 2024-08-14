package com.example.theweatherapp.network.service

import com.example.theweatherapp.domain.model.weather.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service interface for retrieving weather data from the weather API.
 *
 * This interface provides methods to fetch current and hourly weather information
 * based on geographical coordinates, preferred units, and timezone.
 */
interface WeatherService {
    /**
     * Fetches the weather forecast for the specified location and units.
     *
     * @param latitude The latitude of the location for which to fetch the weather data.
     * @param longitude The longitude of the location for which to fetch the weather data.
     * @param temperatureUnit The unit for temperature (e.g., "Celsius", "Fahrenheit").
     * @param windSpeedUnit The unit for wind speed (e.g., "m/s", "km/h").
     * @param timezone The timezone in which the forecast times should be expressed.
     * @return A [WeatherModel] containing the weather forecast data for the specified location.
     */
    @GET(
        "v1/forecast?current=temperature_2m,is_day,relative_humidity_2m,apparent_temperature,weather_code,pressure_msl,wind_speed_10m&hourly=temperature_2m,is_day,weather_code&forecast_days=1",
    )
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: String,
        @Query("wind_speed_unit") windSpeedUnit: String,
        @Query("timezone") timezone: String,
    ): WeatherModel
}
