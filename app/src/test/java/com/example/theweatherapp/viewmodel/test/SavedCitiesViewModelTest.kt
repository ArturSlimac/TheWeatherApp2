@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.example.theweatherapp.viewmodel.test

import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.test_helpers.MainDispatcherRule
import com.example.theweatherapp.utils.Const
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.whenever

class SavedCitiesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val weatherModel =
        WeatherModel(
            current = null,
            current_units = null,
            hourly = null,
            hourly_units = null,
            city = null,
            timezone = "Europe/London",
            timezone_abbreviation = null,
            cashed = false,
            lastSync = null,
        )
    private val cityItemModel =
        CityItemModel(
            country = "Country",
            name = "City",
            state = "State",
            latitude = 0.0,
            longitude = 0.0,
        )

    private lateinit var viewModel: SavedCitiesViewModel
    private val cityRepository: CityRepository = mock()
    private val weatherRepository: WeatherRepository = mock()
    private val settingsRepository: SettingsRepository = mock()

    private val windSpeedUnitFlow = flowOf(Const.WindSpeedUnit.KMH)
    private val temperatureUnitFlow = flowOf(Const.TemperatureUnit.C)
    private val citiesFromApi = flowOf(Response.Success(CityModel()))
    private val savedCitiesFlow = flowOf(Response.Success(CityModel()))

    @Before
    fun setUp() {
        `when`(settingsRepository.getWindSpeedUnit()).thenReturn(windSpeedUnitFlow)
        `when`(settingsRepository.getTemperatureUnit()).thenReturn(temperatureUnitFlow)
        `when`(cityRepository.getAllSavedCities()).thenReturn(savedCitiesFlow)
        `when`(cityRepository.getCitiesByName(anyString())).thenReturn(citiesFromApi)

        `when`(
            weatherRepository.getWeather(
                city = any(),
                temperatureUnit = anyString(),
                windSpeedUnit = anyString(),
                timezone = anyString(),
            ),
        ).thenReturn(flowOf(Response.Success(weatherModel)))
        viewModel = SavedCitiesViewModel(cityRepository, weatherRepository, settingsRepository)
    }

    @Test
    fun `onSearchTextChange updates search text and triggers search`() =
        runTest {
            val searchText = "New City"
            viewModel.onToggleSearch()
            assertTrue(viewModel.isSearching.value)
            viewModel.onSearchTextChange(searchText)
            advanceUntilIdle()
            assertEquals(searchText, viewModel.searchText.value)
            verify(cityRepository).getCitiesByName(searchText)
        }

    @Test
    fun `onCitySelected updates selected city and weather state`() =
        runTest {
            viewModel.onCitySelected(cityItemModel, weatherModel)

            assertEquals(cityItemModel, viewModel.selectedCity.value)
            assertEquals(Response.Success(weatherModel), viewModel.weatherState.value)
            assertFalse(viewModel.shouldFetchWeather)
        }

    @Test
    fun `onSearchCitySelected sets flag to fetch weather for selected city`() =
        runTest {
            whenever(cityRepository.isCitySaved(cityItemModel)).thenReturn(true)
            viewModel.onSearchCitySelected(cityItemModel)
            advanceUntilIdle()
            assertEquals(cityItemModel, viewModel.selectedCity.value)
            assertTrue(viewModel.shouldFetchWeather)
        }

    @Test
    fun `onToggleSearch activates and deactivates search mode`() =
        runTest {
            viewModel.onToggleSearch()
            assertTrue(viewModel.isSearching.value)

            viewModel.onToggleSearch()
            assertFalse(viewModel.isSearching.value)
        }

    @Test
    fun `onDeleteCity deletes selected city and reloads saved cities weather`() =
        runTest {
            viewModel.onCitySelected(cityItemModel, weatherModel)
            viewModel.onDeleteCity()
            advanceUntilIdle()
            verify(cityRepository).deleteCity(cityItemModel)
            // getAllSavedCities was called twice (once during init(!!!), and once after deleting the city)
            verify(cityRepository, times(2)).getAllSavedCities()
        }

    @Test
    fun `init fetches weather for all saved cities`() =
        runTest {
            advanceUntilIdle()
            verify(cityRepository).getAllSavedCities()
        }
}
