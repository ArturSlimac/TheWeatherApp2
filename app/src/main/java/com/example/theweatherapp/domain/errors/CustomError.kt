package com.example.theweatherapp.domain.errors

sealed class CustomError : Exception() {
    object LocationUnavailable : CustomError() {
        override val message: String
            get() = ErrorCode.LOCATION_ERROR.message
    }

    data class ApiError(
        override val message: String,
    ) : CustomError()

    object CityNotFound : CustomError() {
        override val message: String
            get() = ErrorCode.CITY_ERROR.message
    }
}
