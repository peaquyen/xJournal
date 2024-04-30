package com.github.peaquyen.xJournal.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onMenuClick: () -> Unit,
) {
    TopAppBar(
        title = {
                Text(text = "xJournal")
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
             Icon(
                 imageVector = Icons.Default.DateRange,
                 contentDescription = "Data icon",
                 tint = MaterialTheme.colorScheme.onSurface
             )
        }
    )
}