package com.github.peaquyen.xJournal.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit,
    navigateToWrite: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                onMenuClick = onMenuClick
            )
        },

        // floating action button to navigate to write screen
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToWrite
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New xJournal Icon"
                )
            }
        },
        content = {

        }
    )
}