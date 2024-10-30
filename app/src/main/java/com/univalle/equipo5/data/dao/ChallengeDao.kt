package com.univalle.equipo5.data.dao

import androidx.room.*
import com.univalle.equipo5.model.Challenge

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY id DESC")
    suspend fun getAll(): List<Challenge>

    @Query("SELECT * FROM challenges WHERE id = :challengeId")
    suspend fun getById(challengeId: Int): Challenge?

    @Insert
    suspend fun insert(challenge: Challenge)

    @Update
    suspend fun update(challenge: Challenge)

    @Delete
    suspend fun delete(challenge: Challenge)

    @Query("SELECT * FROM challenges ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomChallenge(): Challenge?
}