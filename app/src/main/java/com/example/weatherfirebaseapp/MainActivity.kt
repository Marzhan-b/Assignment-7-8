package com.example.weatherfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherfirebaseapp.data.firebase.FirebaseService
import com.example.weatherfirebaseapp.data.repository.FavoritesRepository
import com.example.weatherfirebaseapp.ui.screens.FavoritesScreen
import com.example.weatherfirebaseapp.ui.screens.SearchScreen
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

                Surface(color = MaterialTheme.colorScheme.background) {
                    Scaffold { paddingValues ->
                        Column(modifier = Modifier.padding(paddingValues)) {
                            SearchScreen(viewModel = favoritesViewModel)

                            HorizontalDivider(thickness = 2.dp)

                            FavoritesScreen(viewModel = favoritesViewModel)
                        }
                    }
                }
            }
        }
    }
}