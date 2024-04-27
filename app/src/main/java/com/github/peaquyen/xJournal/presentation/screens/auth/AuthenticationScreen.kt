package com.github.peaquyen.xJournal.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.github.peaquyen.xJournal.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    onButtonClicked: () -> Unit, //takes no parameters and returns no result
    messageBarState: () -> Unit
) {
    Scaffold(
        content = {

            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    loadingState = false,
                    onButtonClicked = {}
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("Auth", tokenId)
            messageBarState.addSuccess("Successfully Authenticate!")
        },
        onDialogDismissed = { message ->
            Log.d("Auth", message)
            messageBarState.addError(Exception(message))
        }
    )
}

//This code is a good example of how Jetpack Compose can be used to create a clean and
// understandable UI structure