package com.example.theweatherapp.utils

object Const {
    const val WEATHER_API = "https://api.open-meteo.com"
    const val NINJAS_API_KEY = "/CFxD+vTod/vgg7uL6TZ8g==JLBXALLi3YindFLx"
    const val CITY_API = "https://api.api-ninjas.com"

    enum class WindSpeedUnit(
        val unit: String,
    ) {
        MS("ms"),
        MPH("mph"),
        KMH("kmh"),
        ;

        companion object {
            fun fromString(unit: String): WindSpeedUnit? = entries.find { it.unit == unit }
        }
    }

    enum class TemperatureUnit(
        val unit: String,
    ) {
        F("fahrenheit"),
        C("celsius"),
        ;

        companion object {
            fun fromString(unit: String): TemperatureUnit? = entries.find { it.unit == unit }
        }
    }
}
