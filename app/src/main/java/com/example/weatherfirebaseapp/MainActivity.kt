package com.example.weatherfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.database.FirebaseDatabase
import com.example.weatherfirebaseapp.ui.screens.WeatherScreen
import com.example.weatherfirebaseapp.ui.theme.WeatherFirebaseAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.google.firebase.auth.FirebaseAuth.getInstance().signInAnonymously()
        setContent {
            WeatherFirebaseAppTheme {
                Scaffold { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        WeatherScreen()
                    }
                }
            }
        }
        val url = "https://weatherfirebaseapp-daa35-default-rtdb.europe-west1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("test_connection")

        ref.setValue("Connection Success!")
    }
}
