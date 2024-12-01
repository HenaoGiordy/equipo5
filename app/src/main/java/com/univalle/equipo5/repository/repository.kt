package com.univalle.equipo5.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.univalle.equipo5.model.Challenge
import kotlinx.coroutines.tasks.await


class ChallengeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun insertChallenge(challenge: Challenge): String? {
        return try {
            val docRef = firestore.collection("challenges").add(challenge).await()
            challenge.id = docRef.id // Asignar el ID al campo `id` del objeto
            docRef.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllChallenges(): List<Challenge> {
        return try {
            val snapshot = firestore.collection("challenges").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Challenge::class.java)?.apply { id = doc.id }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun updateChallenge(challenge: Challenge) {
        try {
            if (challenge.id != null) {
                firestore.collection("challenges").document(challenge.id!!).set(challenge).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteChallenge(challenge: Challenge) {
        try {
            if (challenge.id != null) {
                firestore.collection("challenges").document(challenge.id!!).delete().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}