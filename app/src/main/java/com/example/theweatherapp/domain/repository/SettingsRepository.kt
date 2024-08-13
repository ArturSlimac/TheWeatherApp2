package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.utils.Const
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user settings related to weather units.
 *
 * This repository provides methods for retrieving and saving the user's preferred units
 * for wind speed and temperature.
 */
interface SettingsRepository {
    /**
     * Retrieves the currently selected wind speed unit.
     *
     * @return A [Flow] emitting the current [Const.WindSpeedUnit] selected by the user.
     */
    fun getWindSpeedUnit(): Flow<Const.WindSpeedUnit>

    /**
     * Retrieves the currently selected temperature unit.
     *
     * @return A [Flow] emitting the current [Const.TemperatureUnit] selected by the user.
     */
    fun getTemperatureUnit(): Flow<Const.TemperatureUnit>

    /**
     * Saves the user's preferred wind speed unit.
     *
     * @param unit The [Const.WindSpeedUnit] to be saved.
     */
    suspend fun saveWindSpeedUnit(unit: Const.WindSpeedUnit)

    /**
     * Saves the user's preferred temperature unit.
     *
     * @param unit The [Const.TemperatureUnit] to be saved.
     */
    suspend fun saveTemperatureUnit(unit: Const.TemperatureUnit)
}
