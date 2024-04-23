package com.github.peaquyen.xJournal.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen() {
    Scaffold(
        content = {
            AuthenticationContent(
                loadingState = false,
                onButtonClicked = {}
            )
        }
    )
}