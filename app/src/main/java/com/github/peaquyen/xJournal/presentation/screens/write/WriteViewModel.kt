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
import com.github.peaquyen.xJournal.util.Constants
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.github.peaquyen.xJournal.util.getCurrentDateTime
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: JournalRepository,
): ViewModel() {
    val galleryState = GalleryState()
    private val _selectedJournal = MutableLiveData<Journal?>()

    val selectedJournal: LiveData<Journal?> get() = _selectedJournal

    val currentTime = getCurrentDateTime()

    init {
        val journalId = savedStateHandle.get<String>(WRITE_SCREEN_ARGUMENT_KEY)
        Log.d("Screen.Write", "passJournalId: $journalId")

        if (journalId != null) {
            getJournal(journalId)
        } else {
            initializeSelectedJournal()
        }
    }

    private fun initializeSelectedJournal() {
        if (_selectedJournal.value == null) {
            _selectedJournal.value = Journal(
                id = UUID.randomUUID().toString(),
                ownerId = "663096cc65679e3f70503509",
                feeling = "Neutral",
                title = "",
                description = "",
                images = listOf(),
                date = currentTime
            )
        }
    }

    private fun getJournal(id: String) {
        viewModelScope.launch {
            try {
                val journal = repository.getJournal(id)
                _selectedJournal.postValue(journal)
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Error getting journal: ", e)
            }
        }
    }

    fun insertJournal(journal: Journal) {
        viewModelScope.launch {
            try {
                Log.d("WriteViewModel", "title: $journal")
                val response = repository.insertJournal(journal)
                _selectedJournal.postValue(response)
            } catch (e: Exception) {
                Log.e("WriteViewModel", "Error inserting journal: ", e)
            }
        }
    }

    fun updateJournal(id: String, journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id
                if (ownerId != null) {
                    val response = repository.updateJournal(id, ownerId, journal)
                    _selectedJournal.postValue(response)
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
            val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id
            try {
                if (ownerId != null) {
                    repository.deleteJournal(id, ownerId)
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
        val ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id
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
}
