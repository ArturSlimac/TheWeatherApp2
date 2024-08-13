package com.example.theweatherapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.theweatherapp.data.device.LocationRepositoryImpl
import com.example.theweatherapp.data.device.SettingsRepositoryImpl
import com.example.theweatherapp.data.local.CityDao
import com.example.theweatherapp.data.local.WeatherDao
import com.example.theweatherapp.data.local.WeatherDatabase
import com.example.theweatherapp.data.repository.CityRepositoryImpl
import com.example.theweatherapp.data.repository.WeatherRepositoryImpl
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.LocationRepository
import com.example.theweatherapp.domain.repository.SettingsRepository
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.service.CityService
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Const.CITY_API
import com.example.theweatherapp.utils.Const.NINJAS_API_KEY
import com.example.theweatherapp.utils.Const.WEATHER_API
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CityApi

/**
 * Dagger module that provides dependencies for the entire application.
 * This module is installed in the SingletonComponent, meaning the provided dependencies
 * will have a singleton lifespan and be available throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    /**
     * Provides the base URL for the weather API.
     *
     * @return The base URL as a string.
     */
    @Provides
    @WeatherApi
    fun provideWeatherAPI(): String = WEATHER_API

    /**
     * Provides the base URL for the city API.
     *
     * @return The base URL as a string.
     */
    @Provides
    @CityApi
    fun provideCityAPI(): String = CITY_API

    /**
     * Provides the API key for the Ninjas API.
     *
     * @return The API key as a string.
     */
    @Provides
    @Named("NINJAS_API_KEY")
    fun provideNinjasApiKey(): String = NINJAS_API_KEY

    /**
     * Creates and provides a Retrofit instance configured for the weather API.
     *
     * @param weatherApi The base URL for the weather API.
     * @return A [Retrofit] instance.
     */
    @Provides
    @WeatherApi
    fun provideWeatherRetrofit(
        @WeatherApi weatherApi: String,
    ): Retrofit = createRetrofit(weatherApi)

    /**
     * Creates and provides a Retrofit instance configured for the city API.
     *
     * @param cityApi The base URL for the city API.
     * @return A [Retrofit] instance.
     */
    @Provides
    @CityApi
    fun provideCityRetrofit(
        @CityApi cityApi: String,
    ): Retrofit = createRetrofit(cityApi)

    /**
     * Creates a [Retrofit] instance with a specified base URL.
     *
     * @param baseUrl The base URL for the API.
     * @return A configured [Retrofit] instance.
     */
    private fun createRetrofit(baseUrl: String): Retrofit {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /**
     * Provides the [WeatherService] for interacting with the weather API.
     *
     * @param retrofit A [Retrofit] instance configured for the weather API.
     * @return A [WeatherService] instance.
     */
    @Provides
    @Singleton
    fun provideWeatherService(
        @WeatherApi retrofit: Retrofit,
    ): WeatherService = retrofit.create(WeatherService::class.java)

    /**
     * Provides the [CityService] for interacting with the city API.
     *
     * @param retrofit A [Retrofit] instance configured for the city API.
     * @return A [CityService] instance.
     */
    @Provides
    @Singleton
    fun provideCityService(
        @CityApi retrofit: Retrofit,
    ): CityService = retrofit.create(CityService::class.java)

    /**
     * Provides the application's database using Room.
     *
     * @param context The application [Context].
     * @return A [WeatherDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): WeatherDatabase =
        Room
            .databaseBuilder(
                context,
                WeatherDatabase::class.java,
                "weather_database",
            ).build()

    /**
     * Provides the [WeatherDao] for accessing weather data in the database.
     *
     * @param database The application's [WeatherDatabase].
     * @return A [WeatherDao] instance.
     */
    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()

    /**
     * Provides the [CityDao] for accessing city data in the database.
     *
     * @param database The application's [WeatherDatabase].
     * @return A [CityDao] instance.
     */
    @Provides
    @Singleton
    fun provideCityDao(database: WeatherDatabase): CityDao = database.cityDao()

    /**
     * Provides the [CityRepository] for managing city data.
     *
     * @param apiKey The API key for the Ninjas API.
     * @param cityService The [CityService] for API interactions.
     * @param cityDao The [CityDao] for database operations.
     * @return A [CityRepository] implementation.
     */
    @Provides
    @Singleton
    fun provideCityRepository(
        @Named("NINJAS_API_KEY") apiKey: String,
        cityService: CityService,
        cityDao: CityDao,
    ): CityRepository = CityRepositoryImpl(apiKey = apiKey, cityService = cityService, cityDao = cityDao)

    /**
     * Provides the [WeatherRepository] for managing weather data.
     *
     * @param weatherService The [WeatherService] for API interactions.
     * @param weatherDao The [WeatherDao] for database operations.
     * @param cityDao The [CityDao] for accessing city data.
     * @param cityRepository The [CityRepository] for managing city data.
     * @param locationRepository The [LocationRepository] for location data.
     * @return A [WeatherRepository] implementation.
     */
    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherService: WeatherService,
        weatherDao: WeatherDao,
        cityDao: CityDao,
        cityRepository: CityRepository,
        locationRepository: LocationRepository,
    ): WeatherRepository =
        WeatherRepositoryImpl(
            weatherService = weatherService,
            weatherDao = weatherDao,
            cityDao = cityDao,
            cityRepository = cityRepository,
            locationRepository = locationRepository,
        )

    /**
     * Provides the [LocationRepository] for managing location data.
     *
     * @param locationProviderClient The [FusedLocationProviderClient] for accessing location data.
     * @return A [LocationRepository] implementation.
     */
    @Provides
    @Singleton
    fun provideLocationRepository(locationProviderClient: FusedLocationProviderClient): LocationRepository =
        LocationRepositoryImpl(locationProviderClient = locationProviderClient)

    /**
     * Provides the [FusedLocationProviderClient] for location services.
     *
     * @param context The application [Context].
     * @return A [FusedLocationProviderClient] instance.
     */
    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Provides the DataStore for managing app preferences.
     *
     * @param context The application [Context].
     * @return A [DataStore] instance for storing preferences.
     */
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings_prefs") },
        )

    /**
     * Provides the [SettingsRepository] for managing app settings.
     *
     * @param dataStore The [DataStore] for storing preferences.
     * @return A [SettingsRepository] implementation.
     */
    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository = SettingsRepositoryImpl(dataStore)
}
