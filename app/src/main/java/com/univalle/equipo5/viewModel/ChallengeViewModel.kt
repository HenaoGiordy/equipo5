package com.univalle.equipo5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.univalle.equipo5.repository.ChallengeRepository
import com.univalle.equipo5.model.Challenge
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _challenges = MutableLiveData<List<Challenge>>()
    val challenges: LiveData<List<Challenge>> get() = _challenges

    init {
        fetchChallenges()
    }

    // Insertar un desafío
    fun insertChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                val docId = challengeRepository.insertChallenge(challenge)
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
                challengeRepository.updateChallenge(challenge)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Eliminar un desafío
    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                challengeRepository.deleteChallenge(challenge)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Obtener un desafío aleatorio
    fun getRandomChallenge(callback: (Challenge?) -> Unit) {
        viewModelScope.launch {
            try {
                val randomChallenge = challengeRepository.getRandomChallenge()
                callback(randomChallenge)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    // Obtener todos los desafíos en tiempo real
    private fun fetchChallenges() {
        challengeRepository.getAllChallenges { challengesList ->
            _challenges.postValue(challengesList)
        }
    }
}
