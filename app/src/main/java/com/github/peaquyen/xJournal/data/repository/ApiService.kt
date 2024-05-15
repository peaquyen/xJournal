package com.github.peaquyen.xJournal.data.repository

import com.github.peaquyen.xJournal.model.Journal
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.time.LocalDate

typealias Journals = Map<LocalDate, List<Journal>>
interface ApiService {
    @Headers("Accept: application/json")

    @GET("journals")
    suspend fun getAllJournals(): Response<List<Journal>>

    @GET("journals/{id}")
    suspend fun getJournal(@Path("id") id: String): Journal

}