package com.github.peaquyen.xJournal.presentation.screens.write

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.data.repository.JournalRepository
import com.github.peaquyen.xJournal.model.Feeling
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import kotlinx.coroutines.launch

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle, // allows to argument passing
    private val repository: JournalRepository
): ViewModel() {
    private val _selectedJournal = MutableLiveData<Journal?>()
    val selectedJournal: MutableLiveData<Journal?> get() = _selectedJournal

    init {
        val journalId = savedStateHandle.get<String>(WRITE_SCREEN_ARGUMENT_KEY)
        Log.d("Screen.Write", "passJournalId: $journalId")

        if (journalId != null) {
            getJournal(journalId)
        }
    }

    private fun getJournal(id: String) {
        viewModelScope.launch {
            try {
                val journal = repository.getJournal(id)
                Log.d("WriteViewModel", "getJournal: $journal")
                setTitle(journal.title)
                setDescription(journal.description)
                setFeeling(Feeling.valueOf(journal.feeling))

                _selectedJournal.value = journal
            } catch (e: Exception) {
                // Handle the error
                Log.e("WriteViewModel", "Error getting journal: ", e)
            }
        }
    }

    public fun setTitle(title: String) {
        val journal = selectedJournal.value
        journal?.title = title
        _selectedJournal.value = journal
    }

    public fun setDescription(description: String) {
        val journal = selectedJournal.value
        journal?.description = description
        _selectedJournal.value = journal
    }

    public fun setFeeling(feeling: Feeling) {
        val journal = selectedJournal.value
        journal?.feeling = feeling.toString()
        _selectedJournal.value = journal
    }
}