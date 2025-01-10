package com.toufiq.factsninja.data.repository

import com.toufiq.factsninja.data.local.FactsDao
import com.toufiq.factsninja.data.model.Fact
import com.toufiq.factsninja.data.remote.FactsApi
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class FactsRepository(
    private val api: FactsApi,
    private val dao: FactsDao
) {
    val latestFact: Flow<Fact?> = dao.getLatestFact()

    suspend fun fetchNewFact(): Result<Fact> {
        return try {
            val response = api.getRandomFact()
            val fact = response.first()
            dao.insertFact(fact)
            Result.success(fact)
        } catch (e: IOException) {
            val cachedFact = dao.getRandomFact()
            if (cachedFact != null) {
                Result.success(cachedFact)
            } else {
                Result.failure(e)
            }
        }
    }
} 