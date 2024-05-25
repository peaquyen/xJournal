package com.github.peaquyen.xJournal.data.repository

import com.github.peaquyen.xJournal.model.Journal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDate

typealias Journals = Map<LocalDate, List<Journal>>
interface ApiService {
    @Headers("Accept: application/json")

    @GET("journals/{ownerId}")
    suspend fun getAllJournals(@Path("ownerId") ownerId: String): Response<List<Journal>>
    @GET("journals/{ownerId}/{id}")
    suspend fun getJournal(@Path("id") id: String, @Path("ownerId") ownerId: String?): Response<Journal>
    @POST("journals/")
    suspend fun insertJournal(@Body journal: Journal): Response<Journal>

//    @PUT("journals/{ownerId}/{id}")
//    suspend fun updateJournal(@Path("id") id: String, @Body journal: Journal): Response<Journal>

}