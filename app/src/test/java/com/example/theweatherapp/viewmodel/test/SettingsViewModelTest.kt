package com.example.theweatherapp.viewmodel.test

import app.cash.turbine.test
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.test_helpers.MainDispatcherRule
import com.example.theweatherapp.utils.Const
import com.example.theweatherapp.viewmodel.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val settingsRepository: SettingsRepository = mock()

    private val windSpeedUnitFlow = MutableStateFlow(Const.WindSpeedUnit.MS)
    private val temperatureUnitFlow = MutableStateFlow(Const.TemperatureUnit.C)

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        `when`(settingsRepository.getWindSpeedUnit()).thenReturn(windSpeedUnitFlow)
        `when`(settingsRepository.getTemperatureUnit()).thenReturn(temperatureUnitFlow)

        viewModel = SettingsViewModel(settingsRepository)
    }

    @Test
    fun `currentWindSpeedUnit should emit initial value from repository`(): Unit =
        runTest {
            // Test the initial value of currentWindSpeedUnit
            viewModel.currentWindSpeedUnit.test {
                assertEquals(Const.WindSpeedUnit.MS, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `currentTemperatureUnit should emit initial value from repository`() =
        runTest {
            // Test the initial value of currentTemperatureUnit
            viewModel.currentTemperatureUnit.test {
                assertEquals(Const.TemperatureUnit.C, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `changeWindSpeedUnit should call saveWindSpeedUnit in repository`(): Unit =
        runTest {
            val newUnit = Const.WindSpeedUnit.KMH
            viewModel.changeWindSpeedUnit(newUnit)
            advanceUntilIdle()
            verify(settingsRepository).saveWindSpeedUnit(newUnit)
        }

    @Test
    fun `changeTemperatureUnit should call saveTemperatureUnit in repository`() =
        runTest {
            val newUnit = Const.TemperatureUnit.F

            viewModel.changeTemperatureUnit(newUnit)
            advanceUntilIdle()
            verify(settingsRepository).saveTemperatureUnit(newUnit)
        }

    @Test
    fun `currentWindSpeedUnit should emit updated value when repository value changes`() =
        runTest {
            val newUnit = Const.WindSpeedUnit.KMH

            viewModel.currentWindSpeedUnit.test {
                assertEquals(Const.WindSpeedUnit.MS, awaitItem())

                windSpeedUnitFlow.value = newUnit

                assertEquals(newUnit, awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `currentTemperatureUnit should emit updated value when repository value changes`() =
        runTest {
            val newUnit = Const.TemperatureUnit.F

            viewModel.currentTemperatureUnit.test {
                assertEquals(Const.TemperatureUnit.C, awaitItem())

                temperatureUnitFlow.value = newUnit

                assertEquals(newUnit, awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }
}
