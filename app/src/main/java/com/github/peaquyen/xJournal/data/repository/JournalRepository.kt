package com.github.peaquyen.xJournal.data.repository

import android.util.Log
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.util.Constants
import io.realm.kotlin.mongodb.App
import retrofit2.Response

class JournalRepository {
    private val apiService: ApiService = ApiClient.getRetroInstance().create(ApiService::class.java)

    suspend fun getJournal(id: String): Journal {
        val response: Response<Journal> = try {
            val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id
            apiService.getJournal(id, ownerId)
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

   suspend fun insertJournal(journal: Journal): Journal {
        Log.d("JournalRepository", "Inserting journal: $journal")
       val response: Response<Journal> = try {
            apiService.insertJournal(journal)
        } catch (e: Exception) {
            Log.e("JournalRepository", "Network call failed", e)
            throw Exception("Network call failed: ${e.message}", e)
        }

        if (response.isSuccessful) {
            val insertedJournal = response.body()
            if (insertedJournal != null) {
                Log.d("JournalRepository", "Journal: $insertedJournal")
                return insertedJournal
            } else {
                Log.e("JournalRepository", "Journal is null")
                throw Exception("Journal is null")
            }
        } else {
            Log.e("JournalRepository", "Error: ${response.code()}")
            Log.e("JournalRepository", "Error body: ${response.errorBody()?.string()}")
            throw Exception("Error: ${response.code()}")
        }
   }

    suspend fun updateJournal(id: String ,ownerId: String, journal: Journal): Journal {
        Log.d("JournalRepository", "Updating journal: $journal")
        Log.d("JournalRepository", "Journal Id: $id")
        val response: Response<Journal> = try {
            apiService.updateJournal(ownerId, id, journal)
        } catch (e: Exception) {
            Log.e("JournalRepository", "Network call failed", e)
            throw Exception("Network call failed: ${e.message}", e)
        }

        if (response.isSuccessful) {
            val updatedJournal = response.body()
            if (updatedJournal != null) {
                Log.d("JournalRepository", "Journal: $updatedJournal")
                return updatedJournal
            } else {
                Log.e("JournalRepository", "Journal is null")
                throw Exception("Journal is null")
            }
        } else {
            Log.e("JournalRepository", "Error: ${response.code()}")
            Log.e("JournalRepository", "Error body: ${response.errorBody()?.string()}")
            throw Exception("Error: ${response.code()}")
        }
    }
}