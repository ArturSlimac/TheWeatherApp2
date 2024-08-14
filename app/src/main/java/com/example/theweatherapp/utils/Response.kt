package com.example.theweatherapp.utils

/**
 * A sealed class representing the possible states of a response.
 *
 * This class is used to encapsulate different outcomes of an operation, typically
 * for handling network requests or asynchronous operations. It provides a way to
 * represent loading, successful results, and failure states in a type-safe manner.
 *
 * @param T The type of the data contained in the response. This is a generic type
 *          that can be specified when creating instances of [Success].
 */
sealed class Response<out T> {
    /**
     * Represents the loading state of the response.
     *
     * This state indicates that the operation is in progress and data is not yet available.
     */
    object Loading : Response<Nothing>()

    /**
     * Represents a successful response containing data.
     *
     * @property data The data retrieved from the operation. It can be null if no data is available.
     */
    data class Success<out T>(
        val data: T?,
    ) : Response<T>()

    /**
     * Represents a failed response.
     *
     * @property e The exception that occurred during the operation, if any. It can be null if no exception was thrown.
     */
    data class Failure(
        val e: Exception?,
    ) : Response<Nothing>()
}
