package com.univalle.equipo5.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.univalle.equipo5.repository.ChallengeRepository
import com.univalle.equipo5.model.Challenge
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()
    private val _challenges = MutableLiveData<List<Challenge>>()
    val challenges: LiveData<List<Challenge>> get() = _challenges

    init {
        fetchChallenges() // Configura el listener en tiempo real al inicializar el ViewModel
    }

    fun insertChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                val docRef = firestore.collection("challenges").add(challenge).await()
                challenge.id = docRef.id
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Actualizar un desafío en Room y Firestore
    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                if (challenge.id != null) {
                    firestore.collection("challenges").document(challenge.id!!).set(challenge).await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Eliminar un desafío en Room y Firestore
    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                if (challenge.id != null) {
                    firestore.collection("challenges").document(challenge.id!!).delete().await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Obtener un desafío aleatorio desde Room
    fun getRandomChallenge(callback: (Challenge?) -> Unit) {
        viewModelScope.launch {
            try {
                val challengesSnapshot = firestore.collection("challenges").get().await()
                val challenges = challengesSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Challenge::class.java)?.apply { id = doc.id }
                }

                // Selecciona un desafío aleatorio de la lista
                val randomChallenge = if (challenges.isNotEmpty()) {
                    challenges.random()
                } else {
                    null // Si no hay desafíos disponibles
                }

                callback(randomChallenge)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null) // En caso de error, devuelve null
            }
        }
    }

    // Obtener todos los desafíos desde Room y Firestore
    private fun fetchChallenges() {
        firestore.collection("challenges").addSnapshotListener { snapshot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val challengesList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Challenge::class.java)?.apply { id = doc.id }
                }
                _challenges.postValue(challengesList)
            }
        }
    }


    // Guardar un desafío en Firestore
    private suspend fun saveChallengeToFirestore(challenge: Challenge): String? {
        return try {
            val challengeRef = firestore.collection("challenges").add(challenge).await()
            challengeRef.id  // Devuelve el ID generado por Firestore
        } catch (e: Exception) {
            e.printStackTrace()
            null  // En caso de error, devuelve null
        }
    }

}
