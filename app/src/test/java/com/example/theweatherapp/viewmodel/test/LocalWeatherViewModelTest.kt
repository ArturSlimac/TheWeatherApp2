@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.example.theweatherapp.viewmodel.test

import com.example.theweatherapp.domain.errors.CustomError
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.test_helpers.MainDispatcherRule
import com.example.theweatherapp.utils.Const
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.LocalWeatherViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class LocalWeatherViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LocalWeatherViewModel
    private val weatherRepository: WeatherRepository = mock()
    private val settingsRepository: SettingsRepository = mock()

    private val windSpeedUnitFlow = flowOf(Const.WindSpeedUnit.KMH)
    private val temperatureUnitFlow = flowOf(Const.TemperatureUnit.C)

    private val weatherModel =
        WeatherModel(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            null,
        )

    @Before
    fun setUp() {
        `when`(settingsRepository.getWindSpeedUnit()).thenReturn(windSpeedUnitFlow)
        `when`(settingsRepository.getTemperatureUnit()).thenReturn(temperatureUnitFlow)
        `when`(
            weatherRepository.getWeather(
                city = any(),
                temperatureUnit = anyString(),
                windSpeedUnit = anyString(),
                timezone = anyString(),
            ),
        ).thenReturn(flowOf(Response.Success(weatherModel)))

        viewModel = LocalWeatherViewModel(weatherRepository, settingsRepository)
    }

    @Test
    fun `onPermissionDenied should update weatherState with LocationUnavailable error`() =
        runTest {
            viewModel.onPermissionDenied()
            assertEquals(Response.Failure(CustomError.LocationUnavailable), viewModel.weatherState.value)
        }

    @Test
    fun `fetchWeather should fetch weather data and update weatherState`() =
        runTest {
            viewModel.fetchWeather()
            advanceUntilIdle()
            assertEquals(Response.Success(weatherModel), viewModel.weatherState.value)
        }

    @Test
    fun `fetchWeather should save weather data on success`() =
        runTest {
            viewModel.fetchWeather()
            advanceUntilIdle()
            assertEquals(Response.Success(weatherModel), viewModel.weatherState.value)
            verify(weatherRepository).saveWeather(weatherModel)
        }

    @Test
    fun `fetchWeather should handle failure response correctly`() =
        runTest {
            val failureResponse = Response.Failure(CustomError.ApiError(""))
            `when`(
                weatherRepository.getWeather(
                    city = any(),
                    temperatureUnit = anyString(),
                    windSpeedUnit = anyString(),
                    timezone = anyString(),
                ),
            ).thenReturn(flowOf(failureResponse))
            viewModel.fetchWeather()

            advanceUntilIdle()

            assertEquals(failureResponse, viewModel.weatherState.value)

            verify(weatherRepository, never()).saveWeather(weatherModel)
        }
}
