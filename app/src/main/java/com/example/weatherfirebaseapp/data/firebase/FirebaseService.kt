package com.example.weatherfirebaseapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://weatherfirebaseapp-daa35-default-rtdb.europe-west1.firebasedatabase.app/").reference

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun saveFavoriteCity(cityName: String, note: String) {
        val uid = getCurrentUserId() ?: return
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

    fun observeFavorites(): Flow<List<FavoriteDto>> = callbackFlow {
        val uid = getCurrentUserId() ?: return@callbackFlow
        val userRef = db.child("favorites").child(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(FavoriteDto::class.java) }
                trySend(items)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        userRef.addValueEventListener(listener)
        awaitClose { userRef.removeEventListener(listener) }
    }
    fun deleteFavorite(favoriteId: String) {
        val uid = getCurrentUserId() ?: return
        db.child("favorites").child(uid).child(favoriteId).removeValue()
    }
}