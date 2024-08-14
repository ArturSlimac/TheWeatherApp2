package com.example.theweatherapp.domain.mappers

import com.example.theweatherapp.domain.model.WeatherCityAllTogether
import com.example.theweatherapp.domain.model.city.CityItemEntity
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.helpers.CurrentTemperatureItem
import com.example.theweatherapp.domain.model.helpers.CurrentWeatherItem
import com.example.theweatherapp.domain.model.helpers.ShortWeatherOverview
import com.example.theweatherapp.domain.model.helpers.TemperatureUiDetails
import com.example.theweatherapp.domain.model.helpers.WeatherForecastItem
import com.example.theweatherapp.domain.model.helpers.WeatherType
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

fun WeatherModel.toWeatherEntity(cityId: Int): WeatherEntity =
    WeatherEntity(
        timezone = this.timezone,
        timezoneAbbreviation = this.timezone_abbreviation,
        cityId = cityId,
    )

fun WeatherModel.toHourlyUnitsEntity(weatherId: Int): HourlyUnitsEntity? =
    this.hourly_units?.let {
        HourlyUnitsEntity(
            weatherId = weatherId,
            temperature_2m = it.temperature_2m,
            time = it.time,
            weather_code = it.weather_code,
            is_day = it.is_day,
        )
    }

fun WeatherModel.toHourlyEntity(weatherId: Int): HourlyEntity? =
    this.hourly?.let {
        HourlyEntity(
            weatherId = weatherId,
            time = it.time,
            temperature_2m = it.temperature_2m,
            weather_code = it.weather_code,
            is_day = it.is_day,
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
            is_day = it.is_day,
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
            is_day = it.is_day,
        )
    }

fun WeatherModel.toCityItemEntity(): CityItemEntity? =
    this.city?.let {
        CityItemEntity(
            latitude = it.latitude,
            longitude = it.longitude,
            name = it.name,
            state = it.state,
            country = it.country,
        )
    }

fun WeatherModel.toWeatherForecastItems(): List<WeatherForecastItem> {
    val temperatures = this.hourly?.temperature_2m ?: emptyList()
    val times = this.hourly?.time ?: emptyList()
    val isDays = this.hourly?.is_day?.map { it == 1 } ?: emptyList()
    val weatherTypes = this.hourly?.weather_code ?: emptyList()
    val tempUnit = this.hourly_units?.temperature_2m ?: ""
    val timeUnit = this.hourly_units?.time ?: ""
    val currentTime = LocalTime.now()

    return temperatures.zip(times).zip(isDays).zip(weatherTypes).mapNotNull { (tempTimeIsDay, weatherType) ->
        val (tempTime, isDay) = tempTimeIsDay
        val (temp, time) = tempTime
        val timeOnlyString = time.substringAfter('T')
        val timeOnly = LocalTime.parse(timeOnlyString, DateTimeFormatter.ofPattern("HH:mm"))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        if (timeOnly.isAfter(currentTime)) {
            WeatherForecastItem(
                temperature = Pair(temp.toInt(), tempUnit),
                time = Pair(timeOnly.format(dateTimeFormatter), timeUnit),
                weatherIcon =
                    if (isDay) {
                        WeatherType.fromWmoStandard(weatherType).dayWeatherIcon
                    } else {
                        WeatherType.fromWmoStandard(weatherType).nightWeatherIcon
                    },
                isDay = isDay,
            )
        } else {
            null
        }
    }
}

fun WeatherModel.toCurrentTemperatureItem(): CurrentTemperatureItem {
    val temp = this.current?.temperature_2m?.toInt() ?: 0
    val tempUnit = this.current_units?.temperature_2m ?: ""
    val apparentTemp = this.current?.apparent_temperature?.toInt() ?: 0
    val apparentTempUnit = this.current_units?.apparent_temperature ?: ""
    val tempDetails = TemperatureUiDetails.tempUiDetails(temp, tempUnit)

    return CurrentTemperatureItem(
        temperature2m = Pair(temp, tempUnit),
        apparentTemperature = Pair(apparentTemp, apparentTempUnit),
        temperatureUiDetails = tempDetails,
    )
}

fun WeatherModel.toCurrentWeatherItem(): CurrentWeatherItem {
    val windSpeed = this.current?.wind_speed_10m ?: 0.0
    val windSpeedUnit = this.current_units?.wind_speed_10m ?: ""
    val humidity = this.current?.relative_humidity_2m ?: 0.0
    val humidityUnit = this.current_units?.relative_humidity_2m ?: ""
    val pressure = this.current?.pressure_msl ?: 0.0
    val pressureUnit = this.current_units?.pressure_msl ?: ""
    val wt = this.current?.weather_code
    val isDay = this.current?.is_day == 1

    return CurrentWeatherItem(
        pressure = Pair(pressure, pressureUnit),
        humidity = Pair(humidity, humidityUnit),
        windSpeed = Pair(windSpeed, windSpeedUnit),
        weatherType = WeatherType.fromWmoStandard(wt),
        isDay = isDay,
    )
}

fun WeatherModel.toShortWeatherOverview(): ShortWeatherOverview {
    val cityName = this.city?.name ?: ""
    val temperature2m = this.current?.temperature_2m?.toInt() ?: 0
    val temperature2mUnit = this.current_units?.temperature_2m ?: ""
    val apparentTemperature = this.current?.apparent_temperature?.toInt() ?: 0
    val apparentTemperatureUnit = this.current_units?.apparent_temperature ?: ""

    return ShortWeatherOverview(
        apparentTemperature = Pair(apparentTemperature, apparentTemperatureUnit),
        temperature2m = Pair(temperature2m, temperature2mUnit),
        cityName = cityName,
    )
}

fun WeatherCityAllTogether.toWeatherModel(): WeatherModel =
    WeatherModel(
        lastSync = Date(this.weather.createdAt),
        timezone = this.weather.timezone,
        timezone_abbreviation = this.weather.timezoneAbbreviation,
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
                    is_day = it.is_day,
                )
            },
        city =
            this.city?.let {
                CityItemModel(
                    name = it.name,
                    country = it.country,
                    state = it.state,
                    latitude = it.latitude,
                    longitude = it.longitude,
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

fun CityItemEntity.toCityItemModel() =
    CityItemModel(
        country = country,
        name = name,
        state = state,
        latitude = latitude,
        longitude = longitude,
    )
