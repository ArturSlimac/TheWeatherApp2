package com.example.theweatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SavedCitiesViewModel
    @Inject
    constructor(
        private val cityRepository: CityRepository,
        private val weatherRepository: WeatherRepository,
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        // Top search bar
        private val _isSearching = mutableStateOf(false)
        val isSearching: State<Boolean> = _isSearching

        private val _searchText = mutableStateOf("")
        val searchText: State<String> = _searchText

        private val _foundCitiesState = mutableStateOf<Response<CityModel>>(Response.Loading)
        val foundCitiesState: State<Response<CityModel>> = _foundCitiesState

        private val _selectedCity = MutableStateFlow<CityItemModel?>(null)
        val selectedCity: StateFlow<CityItemModel?> get() = _selectedCity

        private val _savedCitiesWeatherState = mutableStateOf<Response<List<WeatherModel>>>(Response.Loading)
        val savedCitiesWeatherState: State<Response<List<WeatherModel>>> = _savedCitiesWeatherState

        // Detail screen
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Loading)
        val weatherState: State<Response<WeatherModel>> = _weatherState

        var shouldFetchWeather = false

        init {
            loadSavedCitiesWeather()
        }

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

        fun onSearchTextChange(text: String) {
            _searchText.value = text
            if (_isSearching.value) {
                searchCitiesByName(_searchText.value)
            }
        }

        fun onCitySelected(
            city: CityItemModel,
            weatherModel: WeatherModel,
        ) {
            _selectedCity.value = city
            _weatherState.value = Response.Success(weatherModel)
            shouldFetchWeather = false
        }

        suspend fun onSearchCitySelected(city: CityItemModel) {
            viewModelScope.launch {
                _selectedCity.value = city
                val isSaved =
                    withContext(Dispatchers.IO) {
                        cityRepository.isCitySaved(city)
                    }
                if (isSaved) {
                    city.apply { this.isSaved = true }
                }
                shouldFetchWeather = true
            }
        }

        fun onToggleSearch() {
            _isSearching.value = !_isSearching.value
            if (!_isSearching.value) {
                onSearchTextChange("")
                _foundCitiesState.value = Response.Loading
            }
        }

        fun onSaveCity() {
            val weatherModel = (weatherState.value as? Response.Success<WeatherModel>)?.data
            viewModelScope.launch {
                weatherModel?.let {
                    weatherRepository.saveWeather(it)
                    if (weatherModel.city?.isSaved == true) {
                        return@launch
                    }
                    weatherModel.city.apply { this?.isSaved = true }
                    val currentWeatherModels =
                        (_savedCitiesWeatherState.value as? Response.Success<List<WeatherModel>>)?.data?.toMutableList() ?: mutableListOf()
                    currentWeatherModels.add(it)
                    _savedCitiesWeatherState.value = Response.Success(currentWeatherModels)
                }
            }
        }

        fun onDeleteCity() {
            viewModelScope.launch {
                selectedCity.value?.let {
                    cityRepository.deleteCity(selectedCity.value!!)
                }
                loadSavedCitiesWeather()
            }
        }

        fun formatDate(date: Date?): String =
            if (date != null) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                dateFormat.format(date)
            } else {
                "Unknown"
            }

        private fun searchCitiesByName(name: String) {
            viewModelScope.launch {
                _foundCitiesState.value = Response.Loading
                if (name.isNotEmpty()) {
                    cityRepository.getCitiesByName(name).collect { response ->
                        _foundCitiesState.value = response
                    }
                }
            }
        }

        private fun loadSavedCitiesWeather() {
            viewModelScope.launch {
                try {
                    cityRepository.getAllSavedCities().collect { response ->
                        when (response) {
                            is Response.Success -> {
                                val cities = response.data ?: emptyList()
                                fetchWeatherForCities(cities)
                            }
                            is Response.Failure -> {
                                _savedCitiesWeatherState.value = Response.Failure(response.e)
                            }
                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    _savedCitiesWeatherState.value = Response.Failure(e)
                }
            }
        }

        private suspend fun fetchWeatherForCities(cities: List<CityItemModel>) {
            val windSpeedUnit = settingsRepository.getWindSpeedUnit().first()
            val temperatureUnit = settingsRepository.getTemperatureUnit().first()

            val weatherModels = mutableListOf<WeatherModel>()
            cities.forEach { city ->
                weatherRepository
                    .getWeather(
                        city = city.apply { this.isSaved = true },
                        windSpeedUnit = windSpeedUnit.unit,
                        timezone = "Europe/London",
                        temperatureUnit = temperatureUnit.unit,
                    ).collect { response ->
                        when (response) {
                            is Response.Success -> {
                                response.data?.let { weatherModel ->
                                    weatherModels.add(weatherModel)
                                    weatherRepository.saveWeather(weatherModel)
                                }
                            }
                            is Response.Failure -> {
                                // Handle failure for specific city if needed
                            }
                            else -> {}
                        }
                    }
            }
            _savedCitiesWeatherState.value = Response.Success(weatherModels)
        }
    }
