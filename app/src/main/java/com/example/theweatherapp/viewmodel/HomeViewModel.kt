package com.example.theweatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.WeatherModel
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
    ) : ViewModel() {
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Success(null))
        val weatherState: State<Response<WeatherModel>> = _weatherState

        fun getWeather(
            latitude: Double,
            longitude: Double,
            temperatureUnit: String,
            windSpeedUnit: String,
            timezone: String,
        ) {
            viewModelScope.launch {
                weatherRepository
                    .getWeather(
                        latitude,
                        longitude,
                        temperatureUnit,
                        windSpeedUnit,
                        timezone,
                    ).collect { response ->
                        _weatherState.value = response
                    }
            }
        }
    }
