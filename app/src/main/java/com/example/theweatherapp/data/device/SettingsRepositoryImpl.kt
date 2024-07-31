package com.example.theweatherapp.data.device

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.utils.Const
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : SettingsRepository {
        private val WIND_SPEED_UNIT = stringPreferencesKey("wind_speed_unit")
        private val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")

        override fun getWindSpeedUnit(): Flow<Const.WindSpeedUnit> =
            dataStore.data.map { preferences ->
                preferences[WIND_SPEED_UNIT]?.let {
                    Const.WindSpeedUnit.fromString(
                        it,
                    )
                }
                    ?: Const.WindSpeedUnit.MS
            }

        override fun getTemperatureUnit(): Flow<Const.TemperatureUnit> =
            dataStore.data.map { preferences ->
                preferences[TEMPERATURE_UNIT]?.let { Const.TemperatureUnit.fromString(it) }
                    ?: Const.TemperatureUnit.C
            }

        override suspend fun saveWindSpeedUnit(unit: Const.WindSpeedUnit) {
            dataStore.edit { preferences -> preferences[WIND_SPEED_UNIT] = unit.unit }
        }

        override suspend fun saveTemperatureUnit(unit: Const.TemperatureUnit) {
            dataStore.edit { preferences -> preferences[TEMPERATURE_UNIT] = unit.unit }
        }
    }
