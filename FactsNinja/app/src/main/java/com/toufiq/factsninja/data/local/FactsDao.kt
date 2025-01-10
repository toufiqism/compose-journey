package com.toufiq.factsninja.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.toufiq.factsninja.data.model.Fact
import kotlinx.coroutines.flow.Flow

@Dao
interface FactsDao {
    @Query("SELECT * FROM facts ORDER BY timestamp DESC LIMIT 1")
    fun getLatestFact(): Flow<Fact?>

    @Insert
    suspend fun insertFact(fact: Fact)

    @Query("SELECT * FROM facts ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomFact(): Fact?
} 