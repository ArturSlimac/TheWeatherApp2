package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.utils.Const
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getWindSpeedUnit(): Flow<Const.WindSpeedUnit>

    fun getTemperatureUnit(): Flow<Const.TemperatureUnit>

    suspend fun saveWindSpeedUnit(unit: Const.WindSpeedUnit)

    suspend fun saveTemperatureUnit(unit: Const.TemperatureUnit)
}
