package viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import repository.ChallengeRepository
import model.Challenge
import data.database.AppDatabase

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChallengeRepository
    private val _challenges = MutableLiveData<List<Challenge>>()
    val challenges: LiveData<List<Challenge>> get() = _challenges

    init {
        val challengeDao = AppDatabase.getDatabase(application).ChallengeDao()
        repository = ChallengeRepository(challengeDao)
        fetchChallenges() // Llamar la función para obtener los retos al iniciar
    }

    fun insertChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.insertChallenge(challenge)
            fetchChallenges() // Actualizar la lista después de insertar
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.deleteChallenge(challenge)
            fetchChallenges() // Actualizar la lista después de eliminar
        }
    }

    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.updateChallenge(challenge)
            fetchChallenges() // Actualizar la lista después de editar
        }
    }

    private fun fetchChallenges() {
        viewModelScope.launch {
            val challengeList = repository.getAllChallenges()
            _challenges.postValue(challengeList) // Actualiza los retos en LiveData
        }
    }
}