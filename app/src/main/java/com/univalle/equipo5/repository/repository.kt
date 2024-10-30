package com.univalle.equipo5.repository

import com.univalle.equipo5.model.Challenge
import com.univalle.equipo5.data.dao.ChallengeDao


class ChallengeRepository(private val challengeDao: ChallengeDao) {

    suspend fun getAllChallenges(): List<Challenge> {
        return challengeDao.getAll()
    }

    suspend fun getChallengeById(challengeId: Int): Challenge? {
        return challengeDao.getById(challengeId)
    }

    suspend fun insertChallenge(challenge: Challenge) {
        challengeDao.insert(challenge)
    }

    suspend fun updateChallenge(challenge: Challenge) {
        challengeDao.update(challenge)
    }

    suspend fun deleteChallenge(challenge: Challenge) {
        challengeDao.delete(challenge)
    }

    suspend fun getRandomChallenge(): Challenge? {
        return challengeDao.getRandomChallenge()
    }
}