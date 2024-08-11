package com.example.theweatherapp.domain.formaters

import com.example.theweatherapp.domain.model.weather.WeatherModel
import java.text.SimpleDateFormat
import java.util.Locale

fun WeatherModel.formatDate(): String =
    if (this.lastSync != null) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.format(this.lastSync)
    } else {
        "Unknown"
    }
