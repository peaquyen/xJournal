package com.github.peaquyen.xJournal.presentation.screens.home

import ApiClient
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.data.repository.ApiService
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.util.Constants
import com.github.peaquyen.xJournal.util.RequestState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeViewModel: ViewModel() {
    var journals = MutableLiveData<RequestState<Map<LocalDate, List<Journal>>>>()
    init {
        makeApiCall()
    }

    fun getObserveJournals(): LiveData<RequestState<Map<LocalDate, List<Journal>>>> {
        return journals
    }

    private fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id

            val retroInstance = ApiClient.getRetroInstance().create(ApiService::class.java)

            if (ownerId != null) {
                val response = retroInstance.getAllJournals(ownerId)

                if (response.isSuccessful) {
                    Log.d("HomeViewModel", "API call successful: ${response.body()}")
                    val journalList: List<Journal> = response.body() ?: emptyList()

                    // Chuyển đổi từ chuỗi ngày sang LocalDate
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

                    val journalsByDate: Map<LocalDate, List<Journal>> = journalList
                        .sortedByDescending { LocalDateTime.parse(it.date, formatter) }
                        .groupBy { LocalDateTime.parse(it.date, formatter).toLocalDate() }

                    journals.postValue(RequestState.Success(journalsByDate))
                } else {
                    // Update the value of journals with an error
                    Log.e("HomeViewModel", "API request failed with response code: ${response.code()}")
                    journals.postValue(RequestState.Error(Exception("API request failed with response code: ${response.code()}")))
                }
            }
        }
    }

    fun updateJournals() {
        viewModelScope.launch(Dispatchers.IO) {
            makeApiCall()
        }
    }
}
