package com.example.weatherfirebaseapp.navigation

sealed class Screen(val route: String) {
    object Favorites : Screen("favorites")
    object Weather : Screen("weather/{city}") {
        fun create(city: String) = "weather/$city"
    }
}
