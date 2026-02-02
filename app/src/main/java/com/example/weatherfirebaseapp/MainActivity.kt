package com.example.weatherfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherfirebaseapp.ui.screens.WeatherScreen
import com.example.weatherfirebaseapp.ui.theme.WeatherFirebaseAppTheme
import com.example.weatherfirebaseapp.viewModel.WeatherViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherFirebaseAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    WeatherScreen(
                        viewModel = viewModel<WeatherViewModel>()
                    )
                }
            }
        }
    }
}
