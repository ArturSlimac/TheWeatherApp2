package com.example.theweatherapp.domain.model.city

data class CityItemModel(
    val country: String = "Unknown",
    val name: String = "Unknown",
    val state: String? = "Unknown",
    val latitude: Double?,
    val longitude: Double?,
    var isSaved: Boolean = false,
)
