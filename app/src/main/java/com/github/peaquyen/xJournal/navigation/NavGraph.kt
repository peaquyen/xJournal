package com.github.peaquyen.xJournal.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.peaquyen.xJournal.data.repository.JournalRepository
import com.github.peaquyen.xJournal.model.Feeling
import com.github.peaquyen.xJournal.presentation.components.DisplayAlertDialog
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationScreen
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationViewModel
import com.github.peaquyen.xJournal.presentation.screens.home.HomeScreen
import com.github.peaquyen.xJournal.presentation.screens.home.HomeViewModel
import com.github.peaquyen.xJournal.presentation.screens.write.WriteScreen
import com.github.peaquyen.xJournal.presentation.screens.write.WriteViewModel
import com.github.peaquyen.xJournal.presentation.screens.write.WriteViewModelFactory
import com.github.peaquyen.xJournal.util.Constants
import com.github.peaquyen.xJournal.util.Constants.APP_ID
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.github.peaquyen.xJournal.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// draw the navigation graph
// startDestination: the first screen to show
// navController: the navigation controller

@Composable
fun SetUpNavGraph(startDestination: String, navController: NavHostController ) {
    NavHost(startDestination = startDestination ,navController = navController) {
        // define all the screen that our app will have. Each would contain composable function
        authenticationRouter(
            navigateHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRouter(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = { id ->
                navController.navigate(Screen.Write.passJournalId(id))
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRouter(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authenticationRouter(
    navigateHome : () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            // The function takes one argument, tokenId, which is the token ID received from
            // the authentication process.
            onTokenIdReceived = {tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated!")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateHome = {
                navigateHome()
            }
        )
    }
}

fun NavGraphBuilder.homeRouter(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val journals by viewModel.getObserveJournals().observeAsState(initial = RequestState.Loading)
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember{ mutableStateOf(false) }
        val scope = rememberCoroutineScope()


        HomeScreen(
            journals = journals,
            drawerState = drawerState,
            onSignOutClick = {
                signOutDialogOpened = true
            },
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs
        )


        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = {
                signOutDialogOpened = false
            },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.Companion.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        // navigate to auth screen
                        // use withContext to switch to main thread
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRouter(onBackPressed : () -> Unit) {
    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
//        val homeViewModel: HomeViewModel = viewModel()
        var ownerId = App.Companion.create(Constants.APP_ID).currentUser?.id

        val journalId = it.arguments?.getString(WRITE_SCREEN_ARGUMENT_KEY)
        it.savedStateHandle.set(WRITE_SCREEN_ARGUMENT_KEY, journalId)

        val viewModel: WriteViewModel = viewModel(
            factory = WriteViewModelFactory(it.savedStateHandle, JournalRepository())
        )

        val selectedJournal by viewModel.selectedJournal.observeAsState()


        val pagerState = rememberPagerState(pageCount = { Feeling.entries.size })
        val pageNumber by remember{ derivedStateOf{pagerState.currentPage} }
        // WriteScreen
        if (ownerId != null) {
            WriteScreen(
                pagerState = pagerState,
                selectedJournal = selectedJournal,
                moodName = {
                    Feeling.entries[pageNumber].name
                },
                onTitleChanged = {
                    viewModel.setTitle(it)
                },
                onDescriptionChanged = {
                    viewModel.setDescription(it)
                },
                onBackPressed = onBackPressed,
                onDeleteConfirmed = {
                    // delete the journal
                },
                onSaveClicked = {
                    viewModel.insertJournal(it)
                    onBackPressed()
//                    homeViewModel.updateJournals()
                },
                ownerId = ownerId
            )
        }
    }
}