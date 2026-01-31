package com.example.weatherfirebaseapp.domain.model

data class Weather(
    val temperature: Double,
    val windSpeed: Double,
    val forecast: List<DailyForecast>,
    val lastUpdate:String
)

data class DailyForecast(
    val maxTemp: Double,
    val minTemp: Double
)
