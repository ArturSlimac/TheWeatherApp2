package com.example.theweatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The application class for the weather app.
 *
 * This class serves as the entry point for the application and is used to initialize Dagger Hilt
 * for dependency injection. Annotating this class with [HiltAndroidApp] triggers Hilt's code
 * generation, including a base class for the application that uses dependency injection.
 */
@HiltAndroidApp
class TheWeatherApp : Application()
