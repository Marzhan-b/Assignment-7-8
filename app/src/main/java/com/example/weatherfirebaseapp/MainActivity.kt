package com.example.weatherfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.weatherfirebaseapp.data.firebase.FirebaseService
import com.example.weatherfirebaseapp.data.repository.FavoritesRepository
import com.example.weatherfirebaseapp.navigation.Screen
import com.example.weatherfirebaseapp.ui.screens.FavoritesScreen
import com.example.weatherfirebaseapp.ui.screens.WeatherScreen
import com.example.weatherfirebaseapp.ui.theme.WeatherFirebaseAppTheme
import com.example.weatherfirebaseapp.ui.viewmodel.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuth.getInstance().signInAnonymously()

        val service = FirebaseService()
        val repository = FavoritesRepository(service)

        setContent {
            WeatherFirebaseAppTheme {
                val favoritesViewModel: FavoritesViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return FavoritesViewModel(repository) as T
                        }
                    }
                )

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Favorites.route
                ) {

                    composable(Screen.Favorites.route) {
                        FavoritesScreen(
                            viewModel = favoritesViewModel,
                            onCityClick = { city ->
                                navController.navigate(Screen.Weather.create(city))
                            }
                        )
                    }

                    composable(
                        route = Screen.Weather.route,
                        arguments = listOf(navArgument("city") {
                            type = NavType.StringType
                        })
                    ) { backStack ->
                        val city =
                            backStack.arguments?.getString("city") ?: ""
                        WeatherScreen(cityName = city)
                    }
                }
            }
        }
    }
}
