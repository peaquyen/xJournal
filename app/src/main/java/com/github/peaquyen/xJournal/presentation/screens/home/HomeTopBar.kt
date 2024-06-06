package com.github.peaquyen.xJournal.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onTitleClick: () -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
                Text(
                    text = "xJournal",
                    modifier = Modifier.clickable(onClick = onTitleClick)
                )
        },
        // Icon menu
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                 Icon(
                     imageVector = Icons.Default.Menu,
                     contentDescription = "Menu Icon",
                     tint = MaterialTheme.colorScheme.onSurface
                 )
            }
        },
        actions = {
            IconButton(onClick = onCalendarClick) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Calendar Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}