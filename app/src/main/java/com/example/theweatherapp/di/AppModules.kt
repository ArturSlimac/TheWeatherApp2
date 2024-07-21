package com.example.theweatherapp.di

import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.network.repository.WeatherRepositoryImpl
import com.example.theweatherapp.network.service.WeatherService
import com.example.theweatherapp.utils.Const.WEB_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Named("WEB_API")
    fun provideWebAPI(): String = WEB_API

    @Provides
    fun provideRetrofit(
        @Named("WEB_API") webApi: String,
    ): Retrofit {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY),
                ).build()
        return Retrofit
            .Builder()
            .baseUrl(webApi)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)

    @Provides
    fun provideWeatherRepository(weatherService: WeatherService): WeatherRepository = WeatherRepositoryImpl(weatherService = weatherService)
}
