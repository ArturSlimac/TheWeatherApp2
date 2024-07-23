package com.example.theweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.domain.repository.WeatherDao
import com.example.theweatherapp.domain.repository.WeatherDatabase
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.repository.CityRepositoryImpl
import com.example.theweatherapp.network.repository.WeatherRepositoryImpl
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Named("WEATHER_API")
    fun provideWeatherAPI(): String = WEATHER_API

    @Provides
    @Named("CITY_API")
    fun provideCityAPI(): String = CITY_API

    @Provides
    @Named("NINJAS_API_KEY")
    fun provideNinjasApiKey(): String = NINJAS_API_KEY

    @Provides
    @Named("WeatherRetrofit")
    fun provideWeatherRetrofit(
        @Named("WEATHER_API") weatherApi: String,
    ): Retrofit {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
                ).build()
        return Retrofit
            .Builder()
            .baseUrl(weatherApi)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Named("CityRetrofit")
    fun provideCityRetrofit(
        @Named("CITY_API") cityApi: String,
    ): Retrofit {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
                ).build()
        return Retrofit
            .Builder()
            .baseUrl(cityApi)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(
        @Named("WeatherRetrofit") retrofit: Retrofit,
    ): WeatherService = retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideCityService(
        @Named("CityRetrofit") retrofit: Retrofit,
    ): CityService = retrofit.create(CityService::class.java)

    @Provides
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
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()

    @Provides
    fun provideCityRepository(
        @Named("NINJAS_API_KEY") apiKey: String,
        cityService: CityService,
    ): CityRepository = CityRepositoryImpl(apiKey = apiKey, cityService = cityService)

    @Provides
    fun provideWeatherRepository(
        weatherService: WeatherService,
        weatherDao: WeatherDao,
        cityRepository: CityRepository,
    ): WeatherRepository =
        WeatherRepositoryImpl(
            weatherService = weatherService,
            weatherDao = weatherDao,
            cityRepository = cityRepository,
        )

    @Provides
    fun provideLocationProvider(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
}
