package com.example.theweatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Loading)
        val weatherState: State<Response<WeatherModel>> = _weatherState

        suspend fun fetchWeather(city: CityItemModel) {
            val windSpeedUnit = settingsRepository.getWindSpeedUnit().first()
            val temperatureUnit = settingsRepository.getTemperatureUnit().first()
            viewModelScope.launch {
                weatherRepository
                    .getWeather(
                        city = city,
                        windSpeedUnit = windSpeedUnit.unit,
                        timezone = "Europe/London",
                        temperatureUnit = temperatureUnit.unit,
                    ).collect { response ->
                        _weatherState.value = response
                    }
            }
        }
    }
