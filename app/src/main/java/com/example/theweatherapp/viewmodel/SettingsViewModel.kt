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

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        val currentWindSpeedUnit: StateFlow<Const.WindSpeedUnit> =
            settingsRepository
                .getWindSpeedUnit()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Const.WindSpeedUnit.MS)

        val currentTemperatureUnit: StateFlow<Const.TemperatureUnit> =
            settingsRepository
                .getTemperatureUnit()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Const.TemperatureUnit.C)

        val windSpeedUnits = Const.WindSpeedUnit.entries
        val temperatureUnits = Const.TemperatureUnit.entries

        fun changeWindSpeedUnit(unit: Const.WindSpeedUnit) {
            viewModelScope.launch {
                settingsRepository.saveWindSpeedUnit(unit)
            }
        }

        fun changeTemperatureUnit(unit: Const.TemperatureUnit) {
            viewModelScope.launch {
                settingsRepository.saveTemperatureUnit(unit)
            }
        }
    }
