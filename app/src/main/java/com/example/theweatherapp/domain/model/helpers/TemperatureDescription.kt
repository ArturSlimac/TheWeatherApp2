package com.example.theweatherapp.domain.model.helpers

import androidx.annotation.StringRes
import com.example.theweatherapp.R

sealed class TemperatureDescription(
    @StringRes val tempDescription: Int,
) {
    object VerySuperMegaCold : TemperatureDescription(R.string.very_super_mega_cold)

    object VerySuperCold : TemperatureDescription(R.string.very_super_cold)

    object VeryCold : TemperatureDescription(R.string.very_cold)

    object Cold : TemperatureDescription(R.string.cold)

    object ColdOk : TemperatureDescription(R.string.cold_ok)

    object WarmSpring : TemperatureDescription(R.string.warm_spring)

    object Warm : TemperatureDescription(R.string.warm)

    object WarmSummer : TemperatureDescription(R.string.warm_summer)

    object Hot : TemperatureDescription(R.string.hot)

    object VeryHot : TemperatureDescription(R.string.very_hot)

    object VerySuperHot : TemperatureDescription(R.string.very_super_hot)

    object VerySuperMegaHot : TemperatureDescription(R.string.very_super_mega_hot)

    object DefaultExpression : TemperatureDescription(R.string.default_expression)

    companion object {
        fun tempDescription(temp: Int?): TemperatureDescription =
            when {
                (temp in -100..-31) -> VerySuperMegaCold
                (temp in -30..-21) -> VerySuperCold
                (temp in -20..-11) -> VeryCold
                (temp in -10..-1) -> Cold
                (temp in 0..9) -> ColdOk
                (temp in 10..14) -> WarmSpring
                (temp in 15..19) -> Warm
                (temp in 20..24) -> WarmSummer
                (temp in 25..29) -> Hot
                (temp in 30..34) -> VeryHot
                (temp in 35..39) -> VerySuperHot
                (temp in 40..100) -> VerySuperMegaHot

                else -> DefaultExpression
            }
    }
}
