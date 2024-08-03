package com.example.theweatherapp.domain.model.helpers

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.theweatherapp.R

sealed class TemperatureDescription(
    @StringRes val tempDescription: Int,
    val colors: List<Color>,
) {
    object VerySuperMegaCold : TemperatureDescription(
        R.string.very_super_mega_cold,
        (listOf(Color(0xFF3499FF), Color(0xFF3A3985))),
    )

    object VerySuperCold : TemperatureDescription(
        R.string.very_super_cold,
        (listOf(Color(0xFF6EE2F5), Color(0xFF6454F0))),
    )

    object VeryCold : TemperatureDescription(
        R.string.very_cold,
        (listOf(Color(0xFF64E8DE), Color(0xFF8A64EB))),
    )

    object Cold : TemperatureDescription(
        R.string.cold,
        (listOf(Color(0xFF7BF2E9), Color(0xFFB65EBA))),
    )

    data object ColdOk : TemperatureDescription(
        R.string.cold_ok,
        (listOf(Color(0xFFB65EBA), Color(0xFF2E8DE1))),
    )

    object WarmSpring : TemperatureDescription(
        R.string.warm_spring,
        (listOf(Color(0xFFFF6CAB), Color(0xFF7366FF))),
    )

    object Warm : TemperatureDescription(
        R.string.warm,
        (listOf(Color(0xFFff9897), Color(0xFFf650A0))),
    )

    object WarmSummer : TemperatureDescription(
        R.string.warm_summer,
        (listOf(Color(0xFFFFCDA5), Color(0xFFEE4D5F))),
    )

    object Hot : TemperatureDescription(
        R.string.hot,
        (listOf(Color(0xFFffcf1b), Color(0xFFff881b))),
    )

    object VeryHot : TemperatureDescription(
        R.string.very_hot,
        (listOf(Color(0xFFffa62e), Color(0xFFea4d2c))),
    )

    object VerySuperHot : TemperatureDescription(
        R.string.very_super_hot,
        (listOf(Color(0xFFf00B51), Color(0xFF7366ff))),
    )

    object VerySuperMegaHot : TemperatureDescription(
        R.string.very_super_mega_hot,
        (listOf(Color(0xFFf00B51), Color(0xFF7366ff))),
    )

    object DefaultExpression : TemperatureDescription(
        R.string.default_expression,
        (listOf(Color(0xFF00FFED), Color(0xFF00b8ba))),
    )

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
