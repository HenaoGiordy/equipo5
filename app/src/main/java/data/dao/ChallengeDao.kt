package data.dao

import androidx.room.*
import model.Challenge

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
}