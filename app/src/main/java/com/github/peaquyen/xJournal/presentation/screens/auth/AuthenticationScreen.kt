package com.github.peaquyen.xJournal.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.github.peaquyen.xJournal.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingGoogleState: Boolean,
    loadingEmailState: Boolean,
    loadingCreateAccount: Boolean,
    oneTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onEmailPasswordReceived: (String, String) -> Unit,
    onCreateAccount: (String, String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateHome: () -> Unit
) {
    Scaffold(
        // add padding to the top and bottom of the screen
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    loadingGoogleState = loadingGoogleState,
                    onGoogleButtonClicked = onButtonClicked,
                    loadingCreateAccount = loadingCreateAccount,
                    loadingEmailState = loadingEmailState,
                    onEmailSignIn = { email, password ->
                        Log.d("Auth", "Email: $email, Password: $password")
                        onEmailPasswordReceived(email, password)
                    },
                    onCreateAccount = { email, password ->
                        Log.d("Auth", "Create Account with Email: $email, Password: $password")
                        onCreateAccount(email, password)
                    }
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("Auth", tokenId)
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            Log.d("Auth", message)
            onDialogDismissed(message)
            messageBarState.addError(Exception(message))
        }
    )

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            Log.d("Auth", "Authenticated")
            navigateHome()
        }
    }
}
