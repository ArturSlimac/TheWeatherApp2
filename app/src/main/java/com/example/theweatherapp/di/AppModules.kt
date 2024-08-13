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

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @WeatherApi
    fun provideWeatherAPI(): String = WEATHER_API

    @Provides
    @CityApi
    fun provideCityAPI(): String = CITY_API

    @Provides
    @Named("NINJAS_API_KEY")
    fun provideNinjasApiKey(): String = NINJAS_API_KEY

    @Provides
    @WeatherApi
    fun provideWeatherRetrofit(
        @WeatherApi weatherApi: String,
    ): Retrofit = createRetrofit(weatherApi)

    @Provides
    @CityApi
    fun provideCityRetrofit(
        @CityApi cityApi: String,
    ): Retrofit = createRetrofit(cityApi)

    private fun createRetrofit(baseUrl: String): Retrofit {
        /*val loggingLevel =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }*/
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

    @Provides
    @Singleton
    fun provideWeatherService(
        @WeatherApi retrofit: Retrofit,
    ): WeatherService = retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideCityService(
        @CityApi retrofit: Retrofit,
    ): CityService = retrofit.create(CityService::class.java)

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

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()

    @Provides
    @Singleton
    fun provideCityDao(database: WeatherDatabase): CityDao = database.cityDao()

    @Provides
    @Singleton
    fun provideCityRepository(
        @Named("NINJAS_API_KEY") apiKey: String,
        cityService: CityService,
        cityDao: CityDao,
    ): CityRepository = CityRepositoryImpl(apiKey = apiKey, cityService = cityService, cityDao = cityDao)

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

    @Provides
    @Singleton
    fun provideLocationRepository(locationProviderClient: FusedLocationProviderClient): LocationRepository =
        LocationRepositoryImpl(locationProviderClient = locationProviderClient)

    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings_prefs") },
        )

    @Provides
    @Singleton
    fun SettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository = SettingsRepositoryImpl(dataStore)
}
