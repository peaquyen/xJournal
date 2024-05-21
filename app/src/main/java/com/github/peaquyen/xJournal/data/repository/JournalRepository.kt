package com.github.peaquyen.xJournal.data.repository

import android.util.Log
import com.github.peaquyen.xJournal.model.Journal
import retrofit2.Response

class JournalRepository {
    private val apiService: ApiService = ApiClient.getRetroInstance().create(ApiService::class.java)

    suspend fun getJournal(id: String): Journal {
        val response: Response<Journal> = try {
            apiService.getJournal(id)
        } catch (e: Exception) {
            Log.e("JournalRepository", "Network call failed", e)
            throw Exception("Network call failed: ${e.message}", e)
        }

        if (response.isSuccessful) {
            val journal = response.body()
            if (journal != null) {
                return journal
            } else {
                Log.e("JournalRepository", "Journal is null")
                throw Exception("Journal is null")
            }
        } else {
            Log.e("JournalRepository", "Error: ${response.code()}")
            throw Exception("Error: ${response.code()}")
        }
    }
}