package com.example.theweatherapp.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.CityRepository
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
        private val cityRepository: CityRepository,
    ) : ViewModel() {
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Loading)
        val weatherState: State<Response<WeatherModel>> = _weatherState

        private val _permissionGranted = mutableStateOf(false)
        val permissionGranted: State<Boolean> = _permissionGranted

        private val _searchText = mutableStateOf("")
        val searchText: State<String> = _searchText

        private val _foundCitiesState = mutableStateOf<Response<CityModel>>(Response.Loading)
        val foundCitiesState: State<Response<CityModel>> = _foundCitiesState

        fun checkLocationPermission(context: Context) {
            val permission =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            _permissionGranted.value = permission == PackageManager.PERMISSION_GRANTED
        }

        fun onSearchTextChange(text: String) {
            _searchText.value = text
            if (text.length > 2) {
                viewModelScope.launch {
                    cityRepository.getCitiesByName(text).collect { response ->
                        _foundCitiesState.value = response
                    }
                }
            }
        }

        fun fetchWeather(city: CityItemModel? = null) {
            if (_permissionGranted.value) {
                viewModelScope.launch {
                    weatherRepository
                        .getWeather(
                            city = city,
                            windSpeedUnit = Const.WindSpeedUnit.MS,
                            timezone = "Europe/London",
                            temperatureUnit = Const.TemperatureUnit.C,
                        ).collect { response ->
                            _weatherState.value = response
                        }
                }
            }
        }

        fun onPermissionGranted() {
            _permissionGranted.value = true

            fetchWeather()
        }

        fun onPermissionDenied() {
            _permissionGranted.value = false
        }

        fun onPermissionsRevoked() {
            _permissionGranted.value = false
        }
    }
