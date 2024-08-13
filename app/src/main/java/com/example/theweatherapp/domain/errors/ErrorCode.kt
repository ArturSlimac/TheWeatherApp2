package com.example.theweatherapp.domain.errors

enum class ErrorCode(
    val message: String,
) {
    API_TIMEOUT("It's taking longer than expected to get data. Try again later"),
    API_ERROR("There was a problem getting your request. Try again later"),
    NETWORK_ERROR("Couldn't complete this request; Is your Internet connected?"),
    LOCATION_ERROR("Current location is not available"),
    CITY_ERROR("City is not found"),
}
