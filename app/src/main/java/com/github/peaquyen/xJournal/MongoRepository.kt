//package com.github.peaquyen.xJournal
//
//import kotlinx.coroutines.flow.Flow
//import org.mongodb.kbson.ObjectId
//import java.time.ZonedDateTime
//
//interface MongoRepository {
//    fun configureTheRealm()
//    fun getAllJournals(): Flow<Journals>
//    fun getFilteredJournals(zonedDateTime: ZonedDateTime): Flow<Journals>
//    fun getSelectedJournal(journalId: ObjectId): Flow<RequestState<Journal>>
//    suspend fun insertJournal(journal: Journal): RequestState<Journal>
//    suspend fun updateJournal(journal: Journal): RequestState<Journal>
//    suspend fun deleteJournal(id: ObjectId): RequestState<Boolean>
//    suspend fun deleteAllJournals(): RequestState<Boolean>
//}