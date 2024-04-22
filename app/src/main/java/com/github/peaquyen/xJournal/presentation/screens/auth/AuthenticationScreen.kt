package com.github.peaquyen.xJournal.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen() {
    Scaffold(
        content = {
            AuthenticationContent()
        }
    )
}