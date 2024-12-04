package com.univalle.equipo5.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.univalle.equipo5.model.Challenge
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.Query


class ChallengeRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Método para obtener los desafíos
    fun getAllChallenges(callback: (List<Challenge>) -> Unit) {
        firestore.collection("challenges")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val challengesList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Challenge::class.java)?.apply { id = doc.id }
                    }
                    callback(challengesList)
                }
            }
    }

    suspend fun insertChallenge(challenge: Challenge): String? {
        return try {
            challenge.createdAt = System.currentTimeMillis()
            val docRef = firestore.collection("challenges").add(challenge).await()
            challenge.id = docRef.id
            docRef.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
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

    // Agregar el método para obtener un desafío aleatorio
    suspend fun getRandomChallenge(): Challenge? {
        return try {
            val challengesSnapshot = firestore.collection("challenges").get().await()
            val challenges = challengesSnapshot.documents.mapNotNull { doc ->
                doc.toObject(Challenge::class.java)?.apply { id = doc.id }
            }

            // Seleccionar un desafío aleatorio de la lista
            challenges.randomOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}