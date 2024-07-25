package com.example.theweatherapp.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.utils.Const
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
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Loading)
        val weatherState: State<Response<WeatherModel>> = _weatherState

        private val _permissionGranted = mutableStateOf(false)
        val permissionGranted: State<Boolean> = _permissionGranted

        private val _showSearchBar = mutableStateOf(false)
        val showSearchBar: State<Boolean> = _showSearchBar

        fun checkLocationPermission(context: Context) {
            val permission =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            _permissionGranted.value = permission == PackageManager.PERMISSION_GRANTED
            _showSearchBar.value = !_permissionGranted.value
        }

        fun fetchWeather() {
            if (_permissionGranted.value) {
                viewModelScope.launch {
                    weatherRepository
                        .getWeather(
                            windSpeedUnit = Const.WindSpeedUnit.MS,
                            timezone = "Europe/London",
                            temperatureUnit = Const.TemperatureUnit.C,
                        ).collect { response ->
                            _weatherState.value = response
                        }
                }
            } else {
                _showSearchBar.value = true
            }
        }

        fun onPermissionGranted() {
            _permissionGranted.value = true
            _showSearchBar.value = false
            fetchWeather()
        }

        fun onPermissionDenied() {
            _permissionGranted.value = false
            _showSearchBar.value = true
        }

        fun onPermissionsRevoked() {
            _permissionGranted.value = false
            _showSearchBar.value = true
        }
    }
