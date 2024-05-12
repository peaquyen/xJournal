package com.github.peaquyen.xJournal.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.data.repository.Journals
import com.github.peaquyen.xJournal.data.repository.MongoDB
import com.github.peaquyen.xJournal.util.RequestState
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    var journals: MutableState<Journals> = mutableStateOf(RequestState.Idle)

    init {
        observeJournals()
    }

    private fun observeJournals() {
        viewModelScope.launch {
            MongoDB.getAllJournals().collect { result ->
                journals.value = result
            }
        }
    }
}