package com.example.weatherfirebaseapp.util

object CityCoordinates {

    fun get(city: String): Pair<Double, Double>? =
        when (city.trim().lowercase()) {
            "astana" -> 51.1694 to 71.4491
            "almaty" -> 43.2220 to 76.8512
            "shymkent" -> 42.3417 to 69.5901
            "karaganda" -> 49.8019 to 73.1021
            "london" -> 51.5074 to -0.1278
            "new york" -> 40.7128 to -74.0060
            else -> null
        }
}
