package com.example.theweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.utils.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for managing and providing settings-related data for the application.
 *
 * This ViewModel handles the user's settings for wind speed and temperature units. It interacts with the
 * [SettingsRepository] to retrieve and update these settings, and it provides a list of available units for
 * both wind speed and temperature. The ViewModel uses Hilt for dependency injection.
 *
 * @property settingsRepository A [SettingsRepository] for retrieving and saving user settings related to wind speed and temperature units.
 */
@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        /**
         * A [StateFlow] that emits the current wind speed unit selected by the user.
         *
         * This [StateFlow] is initialized with the wind speed unit from the repository and updates automatically
         * whenever the wind speed unit changes. The initial value is [Const.WindSpeedUnit.MS].
         */
        val currentWindSpeedUnit: StateFlow<Const.WindSpeedUnit> =
            settingsRepository
                .getWindSpeedUnit()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Const.WindSpeedUnit.MS)

        /**
         * A [StateFlow] that emits the current temperature unit selected by the user.
         *
         * This [StateFlow] is initialized with the temperature unit from the repository and updates automatically
         * whenever the temperature unit changes. The initial value is [Const.TemperatureUnit.C].
         */
        val currentTemperatureUnit: StateFlow<Const.TemperatureUnit> =
            settingsRepository
                .getTemperatureUnit()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Const.TemperatureUnit.C)

        /**
         * A list of available wind speed units ^[Const.WindSpeedUnit].
         *
         * This list contains all the possible units for wind speed that the user can select from.
         */
        val windSpeedUnits = Const.WindSpeedUnit.entries

        /**
         * A list of available temperature units [Const.TemperatureUnit].
         *
         * This list contains all the possible units for temperature that the user can select from.
         */
        val temperatureUnits = Const.TemperatureUnit.entries

        /**
         * Changes the wind speed unit to the specified [Const.WindSpeedUnit].
         *
         * This method updates the wind speed unit in the repository. The change is applied asynchronously within
         * the [viewModelScope].
         *
         * @param unit The new wind speed unit [Const.WindSpeedUnit] to be set.
         */
        fun changeWindSpeedUnit(unit: Const.WindSpeedUnit) {
            viewModelScope.launch {
                settingsRepository.saveWindSpeedUnit(unit)
            }
        }

        /**
         * Changes the temperature unit to the specified [Const.TemperatureUnit].
         *
         * This method updates the temperature unit in the repository. The change is applied asynchronously within
         * the [viewModelScope].
         *
         * @param unit The new temperature unit [Const.TemperatureUnit] to be set.
         */
        fun changeTemperatureUnit(unit: Const.TemperatureUnit) {
            viewModelScope.launch {
                settingsRepository.saveTemperatureUnit(unit)
            }
        }
    }
