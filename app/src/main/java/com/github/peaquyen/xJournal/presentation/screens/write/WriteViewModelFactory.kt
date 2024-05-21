package com.github.peaquyen.xJournal.presentation.screens.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.peaquyen.xJournal.data.repository.JournalRepository

class WriteViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val repository: JournalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WriteViewModel(savedStateHandle, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}