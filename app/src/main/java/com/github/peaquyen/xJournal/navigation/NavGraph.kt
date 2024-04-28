//The file `NavGraph.kt` is responsible for setting up the navigation graph for the application. It defines the different screens (routes) that the application can navigate to and the relationships between them. Here's a brief overview of its responsibilities:
//
//1. `SetUpNavGraph`: This is a Composable function that sets up the main navigation host for the application. It takes a `startDestination` and a `navController` as parameters. The `startDestination` is the first screen that will be shown when the app starts, and the `navController` is used to control navigation between different screens.
//
//2. `authenticationRouter`: This function adds the authentication screen to the navigation graph. It uses the `composable` function to define a route for the authentication screen. Inside this function, it sets up the necessary state and view model for the authentication screen and then displays the `AuthenticationScreen` Composable.
//
//3. `homeRouter` and `writeRouter`: These functions are placeholders for adding the home and write screens to the navigation graph. They currently do not contain any implementation.
//
//The `authenticationRouter` function also contains commented-out code that seems to be a quick fix for handling `messageBarState`. This might be related to a previous or future task in the development process.

package com.github.peaquyen.xJournal.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

        AuthenticationScreen(
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = {
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        if (it)
                        messageBarState.addSuccess("Successfully Authenticated!")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            }
        ) { message ->
            messageBarState.addError(Exception(message))
        }
    }
}

// Quick fix for messageBarState
//@OptIn(ExperimentalMaterial3Api::class)
//fun NavGraphBuilder.authenticationRouter() {
//    composable(route = Screen.Authentication.route) {
//        val oneTapState = rememberOneTapSignInState()
//        val messageBarState = rememberMessageBarState()
//        val messageBarStateFunction: () -> Unit = { /* Use messageBarState here */ }
//        AuthenticationScreen(
//            loadingState = oneTapState.opened,
//            oneTapState = oneTapState,
//            messageBarState = messageBarStateFunction,
//            onButtonClicked = {
//                oneTapState.open()
//            }
//        )
//    }
//}
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