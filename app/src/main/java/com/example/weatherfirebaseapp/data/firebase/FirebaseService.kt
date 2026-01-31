package com.example.weatherfirebaseapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://weatherfirebaseapp-daa35-default-rtdb.europe-west1.firebasedatabase.app/").reference

    fun saveFavoriteCity(cityName: String, note: String) {
        val uid = auth.currentUser?.uid ?: return

        val favoriteId = db.child("favorites").child(uid).push().key ?: return

        val favorite = FavoriteDto(
            id = favoriteId,
            cityName = cityName,
            note = note,
            createdAt = System.currentTimeMillis(),
            createdBy = uid
        )

        db.child("favorites").child(uid).child(favoriteId).setValue(favorite)
    }
}