package com.example.theweatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for managing and providing weather data to the LocalWeather screen.
 *
 * This ViewModel fetches weather data from a repository, manages the state of the weather data,
 * and handles cases where permissions are denied. It uses Hilt for dependency injection.
 *
 * @property weatherRepository A [WeatherRepository] for fetching and saving weather data.
 * @property settingsRepository A [SettingsRepository] for retrieving user settings such as temperature and wind speed units.
 */
@HiltViewModel
class LocalWeatherViewModel
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        /**
         * Holds the current state of the weather data.
         *
         * This state is updated with different [Response] types such as [Response.Loading], [Response.Success],
         * or [Response.Failure]. It can be observed by the UI to react to changes in the weather data.
         */
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Loading)
        val weatherState: State<Response<WeatherModel>> = _weatherState

        /**
         * Updates the weather state to indicate that location permissions were denied.
         *
         * This method sets the state to [Response.Failure] with a specific [CustomError] indicating
         * that the location is unavailable.
         */
        fun onPermissionDenied() {
            _weatherState.value = Response.Failure(CustomError.LocationUnavailable)
        }

        /**
         * Fetches the weather data from the repository and updates the weather state.
         *
         * This method retrieves the user's preferred units for wind speed and temperature from the settings repository,
         * then fetches the weather data from the weather repository. The fetched weather data is saved if the response
         * is successful.
         */
        fun fetchWeather() {
            viewModelScope.launch {
                val windSpeedUnit = settingsRepository.getWindSpeedUnit().first()
                val temperatureUnit = settingsRepository.getTemperatureUnit().first()
                weatherRepository
                    .getWeather(
                        windSpeedUnit = windSpeedUnit.unit,
                        timezone = "Europe/London",
                        temperatureUnit = temperatureUnit.unit,
                    ).collect { response ->
                        _weatherState.value = response
                    }

                if (_weatherState.value is Response.Success) {
                    (_weatherState.value as Response.Success<WeatherModel>).data?.let { weatherRepository.saveWeather(it) }
                }
            }
        }
    }
