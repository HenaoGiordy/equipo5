package com.univalle.equipo5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univalle.equipo5.model.Challenge
import com.univalle.equipo5.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {

    private val _challenges = MutableLiveData<List<Challenge>>()
    val challenges: LiveData<List<Challenge>> get() = _challenges

    init {
        fetchChallenges()
    }

    fun insertChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.insertChallenge(challenge)
            fetchChallenges()
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.deleteChallenge(challenge)
            fetchChallenges()
        }
    }

    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.updateChallenge(challenge)
            fetchChallenges()
        }
    }

    private fun fetchChallenges() {
        viewModelScope.launch {
            val challengeList = repository.getAllChallenges()
            _challenges.postValue(challengeList)
        }
    }

    fun getRandomChallenge(callback: (Challenge?) -> Unit) {
        viewModelScope.launch {
            val randomChallenge = repository.getRandomChallenge()
            callback(randomChallenge)
        }
    }
}