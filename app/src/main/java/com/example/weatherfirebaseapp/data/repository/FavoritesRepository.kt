package com.example.weatherfirebaseapp.data.repository

import com.example.weatherfirebaseapp.data.firebase.FavoriteDto
import com.example.weatherfirebaseapp.data.firebase.FirebaseService
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val firebaseService: FirebaseService) {

    fun getFavorites(): Flow<List<FavoriteDto>> = firebaseService.observeFavorites()

    fun addFavorite(cityName: String, note: String) {
        firebaseService.saveFavoriteCity(cityName, note)
    }

    fun updateFavoriteNote(id: String, newNote: String) {
        firebaseService.updateFavoriteNote(id, newNote)
    }


    fun deleteFavorite(id: String) {
        firebaseService.deleteFavorite(id)
    }
}