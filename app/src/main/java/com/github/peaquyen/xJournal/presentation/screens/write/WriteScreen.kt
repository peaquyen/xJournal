package com.github.peaquyen.xJournal.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.peaquyen.xJournal.model.Feeling
import com.github.peaquyen.xJournal.model.Journal

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    pagerState : PagerState,
    selectedJournal: Journal?,
    moodName: () -> String,
    onBackPressed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onSaveClicked: (Journal) -> Unit,
    ownerId : String
) {
    LaunchedEffect(key1 = null) {
        selectedJournal?.feeling?.let { Feeling.valueOf(it).ordinal }
            ?.let { pagerState.scrollToPage(it) }
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedJournal = selectedJournal,
                moodName = moodName,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
            )
        },
        content = {
            WriteContent(
                pagerState = pagerState,
                paddingValues = it,
                title = selectedJournal?.title ?: "",
                onTitleChanged = onTitleChanged,
                description = selectedJournal?.description ?: "",
                onDescriptionChanged = onDescriptionChanged,
                onSaveClicked = onSaveClicked,
                ownerId = ownerId
            )}
    )
}