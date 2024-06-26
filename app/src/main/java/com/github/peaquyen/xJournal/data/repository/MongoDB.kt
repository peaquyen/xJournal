package com.github.peaquyen.xJournal.data.repository

import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.util.Constants.APP_ID
import com.github.peaquyen.xJournal.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import java.time.ZoneId
import kotlinx.coroutines.flow.map
import com.github.peaquyen.xJournal.util.toInstant
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

// abstraction layer between the rest of the application and the underlying data source

// This code ensures that when a user logs in for the first time,
// their Realm database downloads and stores locally all journal entries that they created
// (based on the ownerId field matching their user ID). This initial synchronization ensures
// the user has access to their existing journals on the new device.
object MongoDB: MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Journal::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Journal>(query = "ownerId == $0", user.id),
                        name = "User's Journals"
                    )
                }
                .log(LogLevel.ALL) // for debugging
                .build()
            realm = Realm.open(config)
        }
    }

    override fun getAllJournals(): Flow<Journals> {
        return if (user != null) {
            try {
                realm.query<Journal>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }
}

private class UserNotAuthenticatedException: Exception("User is not authenticated")