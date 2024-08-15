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
import javax.inject.Inject

/**
 * [ViewModel] for managing and interacting with saved cities and their weather data.
 *
 * This [ViewModel] handles operations related to saved cities, including searching for cities, selecting cities,
 * and managing weather data associated with saved cities. It also manages the state of the search bar and the
 * loading state of the data.
 *
 * @property cityRepository A [CityRepository] for managing city data.
 * @property weatherRepository A [WeatherRepository] for fetching and saving weather data.
 * @property settingsRepository A [SettingsRepository] for retrieving user settings related to units of measurement.
 */
@HiltViewModel
class SavedCitiesViewModel
    @Inject
    constructor(
        private val cityRepository: CityRepository,
        private val weatherRepository: WeatherRepository,
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        /**
         * A [State] indicating is a user is searching for cities or not. Used to des/activate the searchbar
         */
        private val _isSearching = mutableStateOf(false)
        val isSearching: State<Boolean> = _isSearching

        /**
         * A [State] containing the current search text input by the user.
         */
        private val _searchText = mutableStateOf("")
        val searchText: State<String> = _searchText

        /**
         * A [State] representing the result of the city search operation.
         */
        private val _foundCitiesState = mutableStateOf<Response<CityModel>>(Response.Loading)
        val foundCitiesState: State<Response<CityModel>> = _foundCitiesState

        /**
         * A [StateFlow] that holds the currently selected city.
         */
        private val _selectedCity = MutableStateFlow<CityItemModel?>(null)
        val selectedCity: StateFlow<CityItemModel?> get() = _selectedCity

        /**
         * A [State] representing the weather data for saved cities.
         */
        private val _savedCitiesWeatherState = mutableStateOf<Response<List<WeatherModel>>>(Response.Loading)
        val savedCitiesWeatherState: State<Response<List<WeatherModel>>> = _savedCitiesWeatherState

        /**
         * A [StateFlow] indicating whether the list has to be scrolled to the end.
         */
        private val _isScrolledToEnd = MutableStateFlow(false)
        val isScrolledToEnd: StateFlow<Boolean> = _isScrolledToEnd

        /**
         * A [State] representing the weather data for a [_selectedCity].
         */
        private val _weatherState = mutableStateOf<Response<WeatherModel>>(Response.Loading)
        val weatherState: State<Response<WeatherModel>> = _weatherState

        /**
         * A flag indicating whether the weather data for the selected city should be fetched.
         */
        var shouldFetchWeather = false

        /**
         * Initializes the ViewModel by loading the weather data for saved cities.
         */
        init {
            loadSavedCitiesWeather()
        }

        /**
         * Fetches the weather data for the specified city, used for [_selectedCity].
         *
         * This method retrieves weather information based on the current settings and updates the [_weatherState].
         *
         * @param city The city of type [CityItemModel] for which the weather data is to be fetched.
         */
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

        /**
         * Updates the search text and triggers a search if the search bar is active.
         *
         * @param text The new search text to be updated.
         */
        fun onSearchTextChange(text: String) {
            _searchText.value = text
            if (_isSearching.value) {
                searchCitiesByName(_searchText.value)
            }
        }

        /**
         * Handles the selection of a city and updates the weather state.
         *
         * @param city The selected city [CityItemModel].
         * @param weatherModel The [WeatherModel] associated with the selected city.
         */
        fun onCitySelected(
            city: CityItemModel,
            weatherModel: WeatherModel,
        ) {
            _selectedCity.value = city
            _weatherState.value = Response.Success(weatherModel)
            shouldFetchWeather = false
        }

        /**
         * Handles the selection of a city from the search results.
         *
         * This method updates the selected city and checks if the city is already saved. It also sets a flag to fetch
         * the weather data if required.
         *
         * @param city The city [CityItemModel] selected from the search results.
         */
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

        /**
         * Toggles the search mode and clears the search text if exiting search mode.
         */
        fun onToggleSearch() {
            _isSearching.value = !_isSearching.value
            if (!_isSearching.value) {
                onSearchTextChange("")
                _foundCitiesState.value = Response.Loading
            }
        }

        /**
         * Saves the currently selected city and updates the list of saved cities.
         *
         * The method saves the weather data of the selected city and updates the [_savedCitiesWeatherState] if the city
         * is not already saved.
         */
        fun onSaveCity() {
            val weatherModel = (weatherState.value as? Response.Success<WeatherModel>)?.data
            _isScrolledToEnd.value = true
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

        /**
         * Deletes the currently selected city [_selectedCity] and reloads the saved cities weather data.
         */
        fun onDeleteCity() {
            viewModelScope.launch {
                selectedCity.value?.let {
                    cityRepository.deleteCity(selectedCity.value!!)
                }
                loadSavedCitiesWeather()
            }
        }

        /**
         * Sets the state indicating whether the list has to be scrolled to the end.
         *
         * @param value [Boolean] indicating if the list is scrolled to the end.
         */
        fun setScrolledToEnd(value: Boolean) {
            _isScrolledToEnd.value = value
        }

        /**
         * Searches for cities by their name.
         *
         * This method updates the [_foundCitiesState] with the search results from the city repository.
         *
         * @param name The name of the city to search for.
         */
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

        /**
         * Loads the weather data for all saved cities.
         *
         * This method retrieves all saved cities and fetches their weather data, updating the [_savedCitiesWeatherState]
         * accordingly.
         */
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

        /**
         * Fetches the weather data for a list of cities.
         *
         * This method updates the weather data for each city and saves the weather information in the repository.
         *
         * @param cities The list of cities for which weather data is to be fetched.
         */
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
                                Response.Failure(response.e)
                            }
                            else -> {}
                        }
                    }
            }
            _savedCitiesWeatherState.value = Response.Success(weatherModels)
        }
    }
