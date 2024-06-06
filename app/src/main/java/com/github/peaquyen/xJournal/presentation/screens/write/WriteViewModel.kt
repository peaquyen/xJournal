package com.github.peaquyen.xJournal.presentation.screens.write

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.data.repository.JournalRepository
import com.github.peaquyen.xJournal.model.GalleryImage
import com.github.peaquyen.xJournal.model.GalleryState
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationViewModel
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.github.peaquyen.xJournal.util.getCurrentDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: JournalRepository,
): ViewModel() {
    val galleryState = GalleryState()
    private val _selectedJournal = MutableLiveData<Journal?>()

    val selectedJournal: LiveData<Journal?> get() = _selectedJournal

    val currentTime = getCurrentDateTime()
    val ownerId = AuthenticationViewModel().getUid()
    //val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id

    init {
        val journalId = savedStateHandle.get<String>(WRITE_SCREEN_ARGUMENT_KEY)
        Log.d("WriteViewModel", "passJournalId: $journalId") //null
        Log.d("WriteViewModel", "ownerId: $ownerId")
        if (journalId != null) {
            getJournal(journalId)
        } else {
            initializeSelectedJournal()
        }
    }

    private fun initializeSelectedJournal() {
        if (_selectedJournal.value == null) {
            _selectedJournal.value = ownerId?.let {
                Journal(
                    id = UUID.randomUUID().toString(),
                    ownerId = ownerId,
                    feeling = "Neutral",
                    title = "",
                    description = "",
                    images = listOf(),
                    date = currentTime
                )
            }
        }
    }

    private fun getJournal(id: String) {
        viewModelScope.launch {
            try {
                Log.d("WriteViewModel", "id: $id")
                val journal = repository.getJournal(id)
                _selectedJournal.postValue(journal)
                Log.d("WriteViewModel", "journal: $journal")
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Error getting journal: ", e)
            }
        }
    }

    fun insertJournal(journal: Journal) {
        viewModelScope.launch {
            try {
                Log.d("WriteViewModel", "ownerId: $ownerId")
                Log.d("WriteViewModel", "title: $journal")
                val response = retryIO { repository.insertJournal(journal) }
                _selectedJournal.postValue(response)
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Error inserting journal: ", e)
            }
        }
    }

    fun updateJournal(id: String, journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id
                val ownerId = ownerId

                if (ownerId != null) {
                    val response = retryIO { repository.updateJournal(id, ownerId, journal) }
                    withContext(Dispatchers.Main) {
                        _selectedJournal.postValue(response)
                    }
                } else {
                    Log.e("WriteViewModel", "Owner ID is null")
                }
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Error updating journal: ", e)
            }
        }
    }

    fun deleteJournal(id: String) {
        viewModelScope.launch {
            val ownerId = ownerId
            try {
                if (ownerId != null) {
                    retryIO { repository.deleteJournal(id, ownerId) }
                }
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Error deleting journal: ", e)
            }
        }
    }

    fun setTitle(title: String) {
        _selectedJournal.value = _selectedJournal.value?.copy(title = title)
    }

    fun setDescription(description: String) {
        _selectedJournal.value = _selectedJournal.value?.copy(description = description)
    }

    fun setFeeling(feelingName: String) {
        val currentJournal = _selectedJournal.value
        if (currentJournal != null) {
            _selectedJournal.value = currentJournal.copy(feeling = feelingName)
        }
    }

    fun addImage(imageUri: Uri, imageType: String) {
        val ownerId = ownerId
        val lastPathSegment = imageUri.lastPathSegment ?: "unknown"
        val remoteImagePath =
            "images/$ownerId/$lastPathSegment-${System.currentTimeMillis()}.$imageType"
        Log.d("WriteViewModel", "remoteImagePath: $remoteImagePath")
        galleryState.addImage(
            GalleryImage(
                image = imageUri,
                remoteImagePath = remoteImagePath
            )
        )
        viewModelScope.launch {
            val currentJournal = _selectedJournal.value
            if (currentJournal != null) {
                val images = currentJournal.images.toMutableList()
                images.add(remoteImagePath)
                _selectedJournal.value = currentJournal.copy(images = images)
            }

            Log.d("WriteViewModel", "_selectedJournal: ${_selectedJournal.value}")
        }
    }

    private suspend fun <T> retryIO(
        times: Int = 3,
        initialDelay: Long = 1000,
        maxDelay: Long = 3000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Retrying due to error: ", e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }
}
