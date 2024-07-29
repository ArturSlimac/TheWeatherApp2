package com.example.theweatherapp.domain.errors

enum class ErrorCode(
    val message: String,
) {
    API_TIMEOUT("It's taking longer than expected to get data."),
    API_ERROR("There was a problem getting your request."),
    NETWORK_ERROR("Couldn't complete this request; is your Internet connected?"),
    LOCATION_ERROR("Current location is not available"),
}
