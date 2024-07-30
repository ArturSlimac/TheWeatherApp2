package com.example.theweatherapp.domain.mappers

import com.example.theweatherapp.domain.model.WeatherWithDetails
import com.example.theweatherapp.domain.model.weather.CurrentEntity
import com.example.theweatherapp.domain.model.weather.CurrentModel
import com.example.theweatherapp.domain.model.weather.CurrentUnitsEntity
import com.example.theweatherapp.domain.model.weather.CurrentUnitsModel
import com.example.theweatherapp.domain.model.weather.HourlyEntity
import com.example.theweatherapp.domain.model.weather.HourlyModel
import com.example.theweatherapp.domain.model.weather.HourlyUnitsEntity
import com.example.theweatherapp.domain.model.weather.HourlyUnitsModel
import com.example.theweatherapp.domain.model.weather.WeatherEntity
import com.example.theweatherapp.domain.model.weather.WeatherModel

fun WeatherModel.toEntity(): WeatherEntity =
    WeatherEntity(
        latitude = this.latitude,
        longitude = this.longitude,
        timezone = this.timezone,
        timezoneAbbreviation = this.timezone_abbreviation,
        utcOffsetSeconds = this.utc_offset_seconds,
        elevation = this.elevation,
        generationTimeMs = this.generationtime_ms,
        city = this.city,
    )

fun WeatherModel.toHourlyUnitsEntity(weatherId: Int): HourlyUnitsEntity? =
    this.hourly_units?.let {
        HourlyUnitsEntity(
            weatherId = weatherId,
            temperature_2m = it.temperature_2m,
            time = it.time,
            weather_code = it.weather_code,
        )
    }

fun WeatherModel.toHourlyEntity(weatherId: Int): HourlyEntity? =
    this.hourly?.let {
        HourlyEntity(
            weatherId = weatherId,
            time = it.time,
            temperature_2m = it.temperature_2m,
            weather_code = it.weather_code,
        )
    }

fun WeatherModel.toCurrentUnitsEntity(weatherId: Int): CurrentUnitsEntity? =
    this.current_units?.let {
        CurrentUnitsEntity(
            weatherId = weatherId,
            time = it.time,
            weather_code = it.weather_code,
            temperature_2m = it.temperature_2m,
            apparent_temperature = it.apparent_temperature,
            relative_humidity_2m = it.relative_humidity_2m,
            pressure_msl = it.pressure_msl,
            interval = it.interval,
            wind_speed_10m = it.wind_speed_10m,
        )
    }

fun WeatherModel.toCurrentEntity(weatherId: Int): CurrentEntity? =
    this.current?.let {
        CurrentEntity(
            weatherId = weatherId,
            time = it.time,
            weather_code = it.weather_code,
            temperature_2m = it.temperature_2m,
            apparent_temperature = it.apparent_temperature,
            relative_humidity_2m = it.relative_humidity_2m,
            pressure_msl = it.pressure_msl,
            interval = it.interval,
            wind_speed_10m = it.wind_speed_10m,
        )
    }

fun WeatherWithDetails.toWeatherModel(): WeatherModel =
    WeatherModel(
        latitude = this.weather.latitude,
        longitude = this.weather.longitude,
        city = this.weather.city,
        timezone = this.weather.timezone,
        timezone_abbreviation = this.weather.timezoneAbbreviation,
        elevation = this.weather.elevation,
        generationtime_ms = this.weather.generationTimeMs,
        utc_offset_seconds = this.weather.utcOffsetSeconds,
        current =
            this.currentWeather?.let {
                CurrentModel(
                    weather_code = it.weather_code,
                    pressure_msl = it.pressure_msl,
                    time = it.time,
                    interval = it.interval,
                    temperature_2m = it.temperature_2m,
                    wind_speed_10m = it.wind_speed_10m,
                    relative_humidity_2m = it.relative_humidity_2m,
                    apparent_temperature = it.apparent_temperature,
                )
            },
        current_units =
            this.currentUnits?.let {
                CurrentUnitsModel(
                    weather_code = it.weather_code,
                    pressure_msl = it.pressure_msl,
                    time = it.time,
                    interval = it.interval,
                    temperature_2m = it.temperature_2m,
                    wind_speed_10m = it.wind_speed_10m,
                    relative_humidity_2m = it.relative_humidity_2m,
                    apparent_temperature = it.apparent_temperature,
                )
            },
        hourly =
            this.hourlyWeather?.let {
                HourlyModel(
                    weather_code = it.weather_code,
                    time = it.time,
                    temperature_2m = it.temperature_2m,
                )
            },
        hourly_units =
            this.hourlyUnits?.let {
                HourlyUnitsModel(
                    weather_code = it.weather_code,
                    time = it.time,
                    temperature_2m = it.temperature_2m,
                )
            },
    )
