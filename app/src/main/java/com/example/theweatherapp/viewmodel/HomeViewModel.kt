package com.example.theweatherapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.utils.Const
import com.example.theweatherapp.utils.Response
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Tasks
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
        private val locationProviderClient: FusedLocationProviderClient,
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Success(null))
        val weatherState: State<Response<WeatherModel>> = _weatherState

        fun fetchWeather() {
            viewModelScope.launch {
                val location = getCurrentLocation()
                if (location == null) {
                    _weatherState.value = Response.Failure(Exception("Location not available"))
                } else {
                    getWeather(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        windSpeedUnit = Const.WindSpeedUnit.MS,
                        timezone = "Europe/London",
                        temperatureUnit = Const.TemperatureUnit.C,
                    )
                }
            }
        }

        fun onPermissionDenied() {
            _weatherState.value = Response.Failure(Exception("Permission denied"))
        }

        fun onPermissionsRevoked() {
            _weatherState.value = Response.Failure(Exception("Permissions revoked"))
        }

        private fun getWeather(
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

        @SuppressLint("MissingPermission")
        private suspend fun getCurrentLocation(): Location? =
            withContext(Dispatchers.IO) {
                val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                try {
                    val locationResult =
                        locationProviderClient.getCurrentLocation(
                            accuracy,
                            CancellationTokenSource().token,
                        )
                    Tasks.await(locationResult)
                    locationResult.result
                } catch (e: Exception) {
                    null
                }
            }
    }
