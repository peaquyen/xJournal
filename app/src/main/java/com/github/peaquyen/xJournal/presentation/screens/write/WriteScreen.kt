package com.github.peaquyen.xJournal.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.peaquyen.xJournal.model.Feeling
import com.github.peaquyen.xJournal.model.GalleryState
import com.github.peaquyen.xJournal.model.Journal

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    pagerState: PagerState,
    galleryState: GalleryState,
    selectedJournal: Journal?,
    feelingName: () -> String,
    onBackPressed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onSaveClicked: (Journal) -> Unit,
    ownerId: String,
    onImageSelect : (Uri) -> Unit
) {
    LaunchedEffect(selectedJournal) {
        selectedJournal?.feeling?.let { feeling ->
            Log.d("WriteScreen", "selectedJournal: $selectedJournal")
            val pageIndex = Feeling.entries.indexOfFirst { it.name == feeling }
            if (pageIndex >= 0) {
                pagerState.scrollToPage(pageIndex)
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
                galleryState = galleryState,
                paddingValues = it,
                date = selectedJournal?.date,
                title = selectedJournal?.title ?: "",
                onTitleChanged = onTitleChanged,
                description = selectedJournal?.description ?: "",
                onDescriptionChanged = onDescriptionChanged,
                onSaveClicked = onSaveClicked,
                ownerId = ownerId,
                onImageSelect = onImageSelect
            )
        }
    )
}