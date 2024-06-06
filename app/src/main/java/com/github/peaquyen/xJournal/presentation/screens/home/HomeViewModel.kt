package com.github.peaquyen.xJournal.presentation.screens.home

import ApiClient
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.data.repository.ApiService
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.model.RequestState
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeViewModel: ViewModel() {
    var journals = MutableLiveData<RequestState<Map<LocalDate, List<Journal>>>>()
    var filteredJournals = MutableLiveData<RequestState<List<Journal>>>()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val ownerId: String? = AuthenticationViewModel().getUid()
    init {
        makeApiCall()
    }

    fun getObserveJournals(): LiveData<RequestState<Map<LocalDate, List<Journal>>>> {
        return journals
    }

    fun getFilteredJournals(): LiveData<RequestState<List<Journal>>> {
        return filteredJournals
    }

    private fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            val ownerId = ownerId

            val retroInstance = ApiClient.getRetroInstance().create(ApiService::class.java)

            if (ownerId != null) {
                val response = retroInstance.getAllJournals(ownerId)

                if (response.isSuccessful) {
                    Log.d("HomeViewModel", "API call successful: ${response.body()}")
                    val journalList: List<Journal> = response.body() ?: emptyList()

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

                    val journalsByDate: Map<LocalDate, List<Journal>> = journalList
                        .sortedByDescending {
                            val localDateTime = LocalDateTime.parse(it.date, formatter)
                            val zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                            zonedDateTime.toLocalDateTime()
                        }
                        .groupBy {
                            val localDateTime = LocalDateTime.parse(it.date, formatter)
                            val zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                            zonedDateTime.toLocalDate()
                        }

                    journals.postValue(RequestState.Success(journalsByDate))
                } else {
                    Log.e("HomeViewModel", "API request failed with response code: ${response.code()}")
                    journals.postValue(RequestState.Error(Exception("API request failed with response code: ${response.code()}")))
                }
            }
        }
    }

    fun refreshJournals() {
        makeApiCall()
    }

    fun filterJournalsByDate(selectedDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val ownerId = ownerId

            val retroInstance = ApiClient.getRetroInstance().create(ApiService::class.java)

            if (ownerId != null) {
                val response = retroInstance.getJournalsByDate(ownerId, selectedDate.toString())

                if (response.isSuccessful) {
                    Log.d("HomeViewModel", "API call successful: ${response.body()}")
                    val journalList: List<Journal> = response.body() ?: emptyList()
                    filteredJournals.postValue(RequestState.Success(journalList))
                } else {
                    Log.e("HomeViewModel", "API request failed with response code: ${response.code()}")
                    filteredJournals.postValue(RequestState.Error(Exception("API request failed with response code: ${response.code()}")))
                }
            }
        }
    }

    fun getAllJournals() {
        viewModelScope.launch(Dispatchers.IO) {
            val ownerId = ownerId

            val retroInstance = ApiClient.getRetroInstance().create(ApiService::class.java)

            if (ownerId != null) {
                val response = retroInstance.getAllJournals(ownerId)

                if (response.isSuccessful) {
                    Log.d("HomeViewModel", "API call successful: ${response.body()}")
                    val journalList: List<Journal> = response.body() ?: emptyList()

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

                    val journalsByDate: Map<LocalDate, List<Journal>> = journalList
                        .sortedByDescending {
                            val localDateTime = LocalDateTime.parse(it.date, formatter)
                            val zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                            zonedDateTime.toLocalDateTime()
                        }
                        .groupBy {
                            val localDateTime = LocalDateTime.parse(it.date, formatter)
                            val zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                            zonedDateTime.toLocalDate()
                        }

                    journals.postValue(RequestState.Success(journalsByDate))
                } else {
                    Log.e("HomeViewModel", "API request failed with response code: ${response.code()}")
                    journals.postValue(RequestState.Error(Exception("API request failed with response code: ${response.code()}")))
                }
            }
        }
    }

}
