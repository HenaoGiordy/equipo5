package com.univalle.equipo5.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.univalle.equipo5.model.Challenge
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ChallengeRepository @Inject constructor(
    private val firestore: FirebaseFirestore // Inyectamos FirebaseFirestore aquí
) {

    // Método modificado para obtener los desafíos en tiempo real
    fun getAllChallengesRealTime(callback: (List<Challenge>) -> Unit) {
        firestore.collection("challenges")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Maneja el error
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

    // Agregar el método para obtener un desafío aleatorio
    suspend fun getRandomChallenge(): Challenge? {
        return try {
            val challengesSnapshot = firestore.collection("challenges").get().await()
            val challenges = challengesSnapshot.documents.mapNotNull { doc ->
                doc.toObject(Challenge::class.java)?.apply { id = doc.id }
            }

            // Seleccionar un desafío aleatorio de la lista
            challenges.randomOrNull() // Devuelve un desafío aleatorio o null si no hay desafíos
        } catch (e: Exception) {
            e.printStackTrace()
            null // En caso de error, devuelve null
        }
    }
}