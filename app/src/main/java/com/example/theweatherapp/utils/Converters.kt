package com.example.theweatherapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A class that provides type converters for Room database to handle serialization and deserialization
 * of complex types such as lists of primitive types.
 *
 * Type converters are used by Room to convert complex data types into a format that can be stored
 * in a SQLite database and vice versa.
 */
class Converters {
    /**
     * Converts a [List] of [Double] values to a [String] using Gson serialization.
     *
     * @param value The list of [Double] values to convert. It can be null.
     * @return A [String] representation of the list in JSON format. If the input list is null, returns null.
     */
    @TypeConverter
    fun fromDoubleList(value: List<Double>?): String = Gson().toJson(value)

    /**
     * Converts a [String] representing a JSON array to a [List] of [Double] values using Gson deserialization.
     *
     * @param value The JSON [String] to convert. It can be null.
     * @return A [List] of [Double] values if deserialization is successful, or null if the input string is null.
     */
    @TypeConverter
    fun toDoubleList(value: String): List<Double>? {
        val listType = object : TypeToken<List<Double>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Converts a [List] of [String] values to a [String] using Gson serialization.
     *
     * @param value The list of [String] values to convert. It can be null.
     * @return A [String] representation of the list in JSON format. If the input list is null, returns null.
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String = Gson().toJson(value)

    /**
     * Converts a [String] representing a JSON array to a [List] of [String] values using Gson deserialization.
     *
     * @param value The JSON [String] to convert. It can be null.
     * @return A [List] of [String] values if deserialization is successful, or null if the input string is null.
     */
    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Converts a [List] of [Int] values to a [String] using Gson serialization.
     *
     * @param value The list of [Int] values to convert. It can be null.
     * @return A [String] representation of the list in JSON format. If the input list is null, returns null.
     */
    @TypeConverter
    fun fromIntList(value: List<Int>?): String = Gson().toJson(value)

    /**
     * Converts a [String] representing a JSON array to a [List] of [Int] values using Gson deserialization.
     *
     * @param value The JSON [String] to convert. It can be null.
     * @return A [List] of [Int] values if deserialization is successful, or null if the input string is null.
     */
    @TypeConverter
    fun toIntList(value: String): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
