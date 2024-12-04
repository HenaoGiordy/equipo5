package com.univalle.equipo5.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.univalle.equipo5.repository.ChallengeRepository
import com.univalle.equipo5.model.Challenge
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository // Inyectamos el repositorio
) : ViewModel() {

    private val _challenges = MutableLiveData<List<Challenge>>()
    val challenges: LiveData<List<Challenge>> get() = _challenges

    init {
        fetchChallenges() // Configura el listener en tiempo real al inicializar el ViewModel
    }

    // Insertar un desafío
    fun insertChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                val docId = challengeRepository.insertChallenge(challenge)  // Usamos el repositorio para insertar
                challenge.id = docId
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Actualizar un desafío
    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                challengeRepository.updateChallenge(challenge)  // Usamos el repositorio para actualizar
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Eliminar un desafío
    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                challengeRepository.deleteChallenge(challenge)  // Usamos el repositorio para eliminar
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Obtener un desafío aleatorio
    fun getRandomChallenge(callback: (Challenge?) -> Unit) {
        viewModelScope.launch {
            try {
                val randomChallenge = challengeRepository.getRandomChallenge()  // Usamos el repositorio para obtener el desafío
                callback(randomChallenge)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)  // En caso de error, devolvemos null
            }
        }
    }

    // Obtener todos los desafíos en tiempo real
    private fun fetchChallenges() {
        challengeRepository.getAllChallengesRealTime { challengesList ->
            _challenges.postValue(challengesList)  // Actualiza la lista de desafíos
        }
    }
}
