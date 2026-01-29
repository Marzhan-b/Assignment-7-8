package com.example.weatherfirebaseapp.data.repository

import com.example.weatherfirebaseapp.data.remote.WeatherApi
import com.example.weatherfirebaseapp.data.remote.WeatherDto
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class WeatherRepository {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api: WeatherApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(WeatherApi::class.java)

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherDto {
        return api.getWeather(latitude, longitude)
    }
}