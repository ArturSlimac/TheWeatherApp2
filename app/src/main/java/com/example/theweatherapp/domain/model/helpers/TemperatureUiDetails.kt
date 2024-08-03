package com.example.theweatherapp.domain.model.helpers

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.theweatherapp.R

sealed class TemperatureUiDetails(
    @StringRes val tempDescription: Int,
    val colors: List<Color>,
) {
    object VerySuperMegaCold : TemperatureUiDetails(
        R.string.very_super_mega_cold,
        (listOf(Color(0xFF3499FF), Color(0xFF3A3985))),
    )

    object VerySuperCold : TemperatureUiDetails(
        R.string.very_super_cold,
        (listOf(Color(0xFF6EE2F5), Color(0xFF6454F0))),
    )

    object VeryCold : TemperatureUiDetails(
        R.string.very_cold,
        (listOf(Color(0xFF64E8DE), Color(0xFF8A64EB))),
    )

    object Cold : TemperatureUiDetails(
        R.string.cold,
        (listOf(Color(0xFF7BF2E9), Color(0xFFB65EBA))),
    )

    data object ColdOk : TemperatureUiDetails(
        R.string.cold_ok,
        (listOf(Color(0xFFB65EBA), Color(0xFF2E8DE1))),
    )

    object WarmSpring : TemperatureUiDetails(
        R.string.warm_spring,
        (listOf(Color(0xFFFF6CAB), Color(0xFF7366FF))),
    )

    object Warm : TemperatureUiDetails(
        R.string.warm,
        (listOf(Color(0xFFff9897), Color(0xFFf650A0))),
    )

    object WarmSummer : TemperatureUiDetails(
        R.string.warm_summer,
        (listOf(Color(0xFFFFCDA5), Color(0xFFEE4D5F))),
    )

    object Hot : TemperatureUiDetails(
        R.string.hot,
        (listOf(Color(0xFFffcf1b), Color(0xFFff881b))),
    )

    object VeryHot : TemperatureUiDetails(
        R.string.very_hot,
        (listOf(Color(0xFFffa62e), Color(0xFFea4d2c))),
    )

    object VerySuperHot : TemperatureUiDetails(
        R.string.very_super_hot,
        (listOf(Color(0xFFf00B51), Color(0xFF7366ff))),
    )

    object VerySuperMegaHot : TemperatureUiDetails(
        R.string.very_super_mega_hot,
        (listOf(Color(0xFFf00B51), Color(0xFF7366ff))),
    )

    object DefaultExpression : TemperatureUiDetails(
        R.string.default_expression,
        (listOf(Color(0xFF00FFED), Color(0xFF00b8ba))),
    )

    companion object {
        fun tempUiDetails(temp: Int?): TemperatureUiDetails =
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
