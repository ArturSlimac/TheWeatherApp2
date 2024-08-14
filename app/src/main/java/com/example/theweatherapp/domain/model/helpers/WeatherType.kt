package com.example.theweatherapp.domain.model.helpers

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.theweatherapp.R

/**
 * A sealed class representing various weather types, each with a specific title and icons for both day and night.
 *
 * @property weatherTitle The resource ID [StringRes] for the string representing the title of the weather (e.g., "Clear Sky").
 * @property dayWeatherIcon The resource ID [DrawableRes] for the drawable icon representing the weather during the day.
 * @property nightWeatherIcon The resource ID [DrawableRes] for the drawable icon representing the weather during the night.
 */
sealed class WeatherType(
    @StringRes val weatherTitle: Int,
    @DrawableRes val dayWeatherIcon: Int,
    @DrawableRes val nightWeatherIcon: Int,
) {
    object ClearSky : WeatherType(
        weatherTitle = R.string.clear_sky,
        dayWeatherIcon = R.drawable.ic_clear_day,
        nightWeatherIcon = R.drawable.ic_clear_night,
    )

    object MainlyClear : WeatherType(
        weatherTitle = R.string.mainly_clear,
        dayWeatherIcon = R.drawable.ic_cloudy_day,
        nightWeatherIcon = R.drawable.ic_cloudy_night,
    )

    object PartlyCloudy : WeatherType(
        weatherTitle = R.string.partly_cloudy,
        dayWeatherIcon = R.drawable.ic_cloudy_day,
        nightWeatherIcon = R.drawable.ic_cloudy_night,
    )

    object Overcast : WeatherType(
        weatherTitle = R.string.overcast,
        dayWeatherIcon = R.drawable.ic_cloudy_day,
        nightWeatherIcon = R.drawable.ic_cloudy_night,
    )

    object Foggy : WeatherType(
        weatherTitle = R.string.foggy,
        dayWeatherIcon = R.drawable.ic_very_cloudy,
        nightWeatherIcon = R.drawable.ic_very_cloudy,
    )

    object DepositingRimeFog : WeatherType(
        weatherTitle = R.string.depositing_rime_fog,
        dayWeatherIcon = R.drawable.ic_very_cloudy,
        nightWeatherIcon = R.drawable.ic_very_cloudy,
    )

    object LightDrizzle : WeatherType(
        weatherTitle = R.string.light_drizzle,
        dayWeatherIcon = R.drawable.ic_rainshower_day,
        nightWeatherIcon = R.drawable.ic_rainshower_night,
    )

    object ModerateDrizzle : WeatherType(
        weatherTitle = R.string.moderate_drizzle,
        dayWeatherIcon = R.drawable.ic_rainshower_day,
        nightWeatherIcon = R.drawable.ic_rainshower_night,
    )

    object DenseDrizzle : WeatherType(
        weatherTitle = R.string.dense_drizzle,
        dayWeatherIcon = R.drawable.ic_rainshower_day,
        nightWeatherIcon = R.drawable.ic_rainshower_night,
    )

    object LightFreezingDrizzle : WeatherType(
        weatherTitle = R.string.light_freezing_drizzle,
        dayWeatherIcon = R.drawable.ic_snowy_day,
        nightWeatherIcon = R.drawable.ic_snowy_night,
    )

    object DenseFreezingDrizzle : WeatherType(
        weatherTitle = R.string.dense_freezing_drizzle,
        dayWeatherIcon = R.drawable.ic_heavysnow,
        nightWeatherIcon = R.drawable.ic_heavysnow,
    )

    object SlightRain : WeatherType(
        weatherTitle = R.string.slight_rain,
        dayWeatherIcon = R.drawable.ic_rainy_slight,
        nightWeatherIcon = R.drawable.ic_rainy_slight,
    )

    object ModerateRain : WeatherType(
        weatherTitle = R.string.moderate_rain,
        dayWeatherIcon = R.drawable.ic_rainy,
        nightWeatherIcon = R.drawable.ic_rainy,
    )

    object HeavyRain : WeatherType(
        weatherTitle = R.string.heavy_rain,
        dayWeatherIcon = R.drawable.ic_rainy_heavy,
        nightWeatherIcon = R.drawable.ic_rainy_heavy,
    )

    object HeavyFreezingRain : WeatherType(
        weatherTitle = R.string.heavy_freezing_rain,
        dayWeatherIcon = R.drawable.ic_rainy_heavy,
        nightWeatherIcon = R.drawable.ic_rainy_heavy,
    )

    object SlightSnowFall : WeatherType(
        weatherTitle = R.string.slight_snow_fall,
        dayWeatherIcon = R.drawable.ic_snowy_day,
        nightWeatherIcon = R.drawable.ic_snowy_night,
    )

    object ModerateSnowFall : WeatherType(
        weatherTitle = R.string.moderate_snow_fall,
        dayWeatherIcon = R.drawable.ic_heavysnow,
        nightWeatherIcon = R.drawable.ic_heavysnow,
    )

    object HeavySnowFall : WeatherType(
        weatherTitle = R.string.heavy_snow_fall,
        dayWeatherIcon = R.drawable.ic_heavysnow,
        nightWeatherIcon = R.drawable.ic_heavysnow,
    )

    object SnowGrains : WeatherType(
        weatherTitle = R.string.snow_grains,
        dayWeatherIcon = R.drawable.ic_snow_grains,
        nightWeatherIcon = R.drawable.ic_snow_grains,
    )

    object SlightRainShowers : WeatherType(
        weatherTitle = R.string.slight_rain_showers,
        dayWeatherIcon = R.drawable.ic_rainy_slight,
        nightWeatherIcon = R.drawable.ic_rainy_slight,
    )

    object ModerateRainShowers : WeatherType(
        weatherTitle = R.string.moderate_rain_showers,
        dayWeatherIcon = R.drawable.ic_rainy,
        nightWeatherIcon = R.drawable.ic_rainy,
    )

    object ViolentRainShowers : WeatherType(
        weatherTitle = R.string.violent_rain_showers,
        dayWeatherIcon = R.drawable.ic_rainy_heavy,
        nightWeatherIcon = R.drawable.ic_rainy_heavy,
    )

    object SlightSnowShowers : WeatherType(
        weatherTitle = R.string.slight_snow_showers,
        dayWeatherIcon = R.drawable.ic_snowy_day,
        nightWeatherIcon = R.drawable.ic_snowy_night,
    )

    object HeavySnowShowers : WeatherType(
        weatherTitle = R.string.heavy_snow_showers,
        dayWeatherIcon = R.drawable.ic_heavysnow,
        nightWeatherIcon = R.drawable.ic_heavysnow,
    )

    object ModerateThunderstorm : WeatherType(
        weatherTitle = R.string.moderate_thunderstorm,
        dayWeatherIcon = R.drawable.ic_thunder_day,
        nightWeatherIcon = R.drawable.ic_thunder_night,
    )

    object SlightHailThunderstorm : WeatherType(
        weatherTitle = R.string.slight_hail_thunderstorm,
        dayWeatherIcon = R.drawable.ic_thunder,
        nightWeatherIcon = R.drawable.ic_thunder,
    )

    object HeavyHailThunderstorm : WeatherType(
        weatherTitle = R.string.heavy_hail_thunderstorm,
        dayWeatherIcon = R.drawable.ic_thunder,
        nightWeatherIcon = R.drawable.ic_thunder,
    )

    /**
     * A companion object for the [WeatherType] class that provides a method to map WMO standard weather codes to specific [WeatherType] objects.
     */
    companion object {
        /**
         * Maps a WMO (World Meteorological Organization) weather code to a corresponding [WeatherType].
         *
         * @param code The WMO weather code as an [Int].
         * @return The corresponding [WeatherType] for the given code. Defaults to [ClearSky] if the code is not recognized.
         */
        fun fromWmoStandard(code: Int?): WeatherType =
            when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
    }
}
