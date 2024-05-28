package com.github.peaquyen.xJournal.presentation.screens.write

import android.annotation.SuppressLint
import android.util.Log
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
    pagerState: PagerState,
    selectedJournal: Journal?,
    feelingName: () -> String,
    onBackPressed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onSaveClicked: (Journal) -> Unit,
    ownerId: String
) {
//    LaunchedEffect(key1 = null) {
//        selectedJournal?.feeling?.let { Feeling.valueOf(it).ordinal }
//            ?.let { pagerState.scrollToPage(it) }
//    }

    LaunchedEffect(selectedJournal) {
        selectedJournal?.feeling?.let { feeling ->
            Log.d("WriteScreen", "selectedJournal: $selectedJournal")
            Log.d("WriteScreen", "feeling: $feeling")
            val pageIndex = Feeling.entries.indexOfFirst { it.name == feeling }
            if (pageIndex >= 0) {
                pagerState.scrollToPage(pageIndex)
                Log.d("WriteScreen", "scrollToPage: $pageIndex")
            }
        }
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                selectedJournal = selectedJournal,
                feelingName = feelingName,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
            )
        },
        content = {
            WriteContent(
                pagerState = pagerState,
                paddingValues = it,
                date = selectedJournal?.date,
                title = selectedJournal?.title ?: "",
                onTitleChanged = onTitleChanged,
                description = selectedJournal?.description ?: "",
                onDescriptionChanged = onDescriptionChanged,
                onSaveClicked = onSaveClicked,
                ownerId = ownerId
            )}
    )
}