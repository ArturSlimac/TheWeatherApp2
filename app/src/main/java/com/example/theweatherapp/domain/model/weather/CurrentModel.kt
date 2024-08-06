package com.example.theweatherapp.domain.model.weather

import com.example.theweatherapp.domain.model.helpers.TemperatureUiDetails
import com.example.theweatherapp.domain.model.helpers.WeatherType

data class CurrentModel(
    val apparent_temperature: Double? = null,
    val interval: Int? = null,
    val pressure_msl: Double? = null,
    val relative_humidity_2m: Double? = null,
    val temperature_2m: Double? = null,
    val time: String? = null,
    val weather_code: Int? = null,
    val wind_speed_10m: Double? = null,
) {
    val temperature_details: TemperatureUiDetails
        get() = TemperatureUiDetails.tempUiDetails(temperature_2m?.toInt())

    val weatherType: WeatherType
        get() = WeatherType.fromWmoStandard(weather_code)
}
