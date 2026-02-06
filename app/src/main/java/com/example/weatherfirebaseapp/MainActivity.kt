package com.example.weatherfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherfirebaseapp.data.firebase.FirebaseService
import com.example.weatherfirebaseapp.data.repository.FavoritesRepository
import com.example.weatherfirebaseapp.ui.screens.FavoriteCityItem
import com.example.weatherfirebaseapp.ui.viewmodel.FavoritesViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseService = FirebaseService()
        val repository = FavoritesRepository(firebaseService)
        val viewModel = FavoritesViewModel(repository)

        setContent {
            MaterialTheme {
                WeatherMainScreen(viewModel)
            }
        }
    }
}

@Composable
fun WeatherMainScreen(viewModel: FavoritesViewModel) {

    val favorites by viewModel.favorites.collectAsState()

    var city by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E88E5),
            Color(0xFF90CAF9)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {

        Text(
            text = "☀ Weather Dashboard",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(16.dp))

        // ➕ Add city card
        Card(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {

                Text("Add new city", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Weather note (sunny, rain, snow...)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (city.isNotBlank()) {
                            viewModel.addFavorite(city, note)
                            city = ""
                            note = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Favorite, null)
                    Spacer(Modifier.width(6.dp))
                    Text("Save city")
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "⭐ Favorites (real-time)",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(favorites) { city ->
                FavoriteCityItem(
                    city = city,
                    onDelete = { viewModel.deleteFavorite(city.id ?: "") },
                    onCityClick = {},
                    viewModel = viewModel
                )
            }
        }
    }
}
