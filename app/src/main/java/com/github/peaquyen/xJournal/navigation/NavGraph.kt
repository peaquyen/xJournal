package com.github.peaquyen.xJournal.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationScreen
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationViewModel
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

// draw the navigation graph
// startDestination: the first screen to show
// navController: the navigation controller

@Composable
fun SetUpNavGraph(startDestination: String, navController: NavHostController ) {
    NavHost(startDestination = startDestination ,navController = navController) {
        // define all the screen that our app will have. Each would contain composable function
        authenticationRouter()
        homeRouter()
        writeRouter()
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authenticationRouter() {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        //TODO: app shut even before Auth func
        AuthenticationScreen(
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = {tokenId ->
                viewModel.viewModelScope.launch {
                    viewModel.signInWithMongoAtlas(
                        tokenId = tokenId,
                        onSuccess = {
                            //if (it) {
                                messageBarState.addSuccess("Successfully Authenticated!")
                                viewModel.setLoading(false)
                            //}
                        },
                        onError = {
                            messageBarState.addError(it)
                            viewModel.setLoading(false)
                        }
                    )
                }
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            }
        )
    }
}
fun NavGraphBuilder.homeRouter() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRouter() {
    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}