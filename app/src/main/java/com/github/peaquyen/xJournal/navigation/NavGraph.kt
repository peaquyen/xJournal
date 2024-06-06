package com.github.peaquyen.xJournal.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.peaquyen.xJournal.data.repository.JournalRepository
import com.github.peaquyen.xJournal.model.Feeling
import com.github.peaquyen.xJournal.model.RequestState
import com.github.peaquyen.xJournal.presentation.components.DisplayAlertDialog
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationScreen
import com.github.peaquyen.xJournal.presentation.screens.auth.AuthenticationViewModel
import com.github.peaquyen.xJournal.presentation.screens.home.HomeScreen
import com.github.peaquyen.xJournal.presentation.screens.home.HomeViewModel
import com.github.peaquyen.xJournal.presentation.screens.write.WriteScreen
import com.github.peaquyen.xJournal.presentation.screens.write.WriteViewModel
import com.github.peaquyen.xJournal.presentation.screens.write.WriteViewModelFactory
import com.github.peaquyen.xJournal.util.Constants.APP_ID
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun SetUpNavGraph(startDestination: String, navController: NavHostController) {
    Log.d("SetUpNavGraph", "Setting up NavGraph with startDestination: $startDestination")
    NavHost(startDestination = startDestination, navController = navController) {
        authenticationRouter(
            navigateHome = {
                Log.d("SetUpNavGraph", "Navigating to Home")
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRouter(
            navigateToWrite = {
                Log.d("SetUpNavGraph", "Navigating to Write")
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = { id ->
                Log.d("SetUpNavGraph", "Navigating to Write with args, id: $id")
                navController.navigate(Screen.Write.passJournalId(id))
            },
            navigateToAuth = {
                Log.d("SetUpNavGraph", "Navigating to Auth")
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRouter(
            navController = navController,
            onBackPressed = {
                Log.d("SetUpNavGraph", "Back pressed")
                navController.popBackStack()
            }
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authenticationRouter(navigateHome: () -> Unit) {
    Log.d("authenticationRouter", "Setting up Authentication Router")
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingGoogleState by viewModel.loadingGoogleState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        var ownerId: String

        AuthenticationScreen(
            authenticated = authenticated,
            loadingGoogleState = loadingGoogleState,
            loadingEmailState = viewModel.loadingEmailState.value,
            loadingCreateAccount = viewModel.loadingCreateAccount.value,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                Log.d("authenticationRouter", "Google Sign-In Button Clicked")
                oneTapState.open()
                viewModel.setGoogleLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                Log.d("authenticationRouter", "Token ID received: $tokenId")
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        Log.d("Auth", "Successfully Authenticated!")
                        messageBarState.addSuccess("Successfully Authenticated!")
                        viewModel.setGoogleLoading(false)
                        AuthenticationViewModel.ownerId = App.Companion.create(APP_ID).currentUser?.toString()!!
                    },
                    onError = {
                        Log.d("Auth", "Error: $it")
                        messageBarState.addError(it)
                        viewModel.setGoogleLoading(false)
                    }
                )
                Log.d("authenticationRouter", "Token ID received: $tokenId")
            },
            onEmailPasswordReceived = { email, password ->
                Log.d("authenticationRouter", "Email and Password received: $email")
                viewModel.signInWithEmail(
                    email = email,
                    password = password,
                    onSuccess = {
                        Log.d("Auth", "Successfully Authenticated with Email!")
                        messageBarState.addSuccess("Successfully Authenticated!")
                        viewModel.setEmailLoading(false)
                        AuthenticationViewModel.ownerId = AuthenticationViewModel().getUid().toString()
                    },
                    onError = {
                        Log.d("Auth", "Email Sign-In Error: $it")
                        messageBarState.addError(it)
                        viewModel.setEmailLoading(false)
                    }
                )
            },
            onCreateAccount = { email, password ->
                Log.d("authenticationRouter", "Create Account with Email: $email")
                viewModel.createAccount(
                    email = email,
                    password = password,
                    onSuccess = {
                        Log.d("Auth", "Account Created!")
                        messageBarState.addSuccess("Account Created! Please check your email for verification.")
                        viewModel.setCreateAccountLoading(false)
                    },
                    onError = {
                        Log.d("Auth", "Create Account Error: $it")
                        messageBarState.addError(it)
                        viewModel.setCreateAccountLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                Log.d("authenticationRouter", "Dialog Dismissed: $message")
                messageBarState.addError(Exception(message))
                viewModel.setCreateAccountLoading(false)
            },
            navigateHome = {
                Log.d("authenticationRouter", "Navigating Home")
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
    Log.d("homeRouter", "Setting up Home Router")
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val journals by viewModel.getObserveJournals().observeAsState(initial = RequestState.Loading)
        val filteredJournals by viewModel.getFilteredJournals().observeAsState(initial = RequestState.Loading)
        Log.d("HomeRouter", "journals: $journals")
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

        LaunchedEffect(Unit) {
            Log.d("homeRouter", "LaunchedEffect: Refreshing journals")
            viewModel.refreshJournals() // Fetch the latest data when the screen is composed
        }

        HomeScreen(
            journals = journals,
            drawerState = drawerState,
            onSignOutClick = {
                Log.d("homeRouter", "Sign Out Clicked")
                signOutDialogOpened = true
            },
            onMenuClick = {
                Log.d("homeRouter", "Menu Clicked")
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = {
                Log.d("homeRouter", "Navigating to Write Screen")
                navigateToWrite()
            },
            navigateToWriteWithArgs = {
                Log.d("homeRouter", "Navigating to Write Screen with args: $it")
                navigateToWriteWithArgs(it)
            },
            context = LocalContext.current,
            filteredJournals = (filteredJournals as? RequestState.Success)?.data,
            selectedDate = selectedDate,
            onDateSelected = { date ->
                Log.d("homeRouter", "Date Selected: $date")
                selectedDate = date
                viewModel.filterJournalsByDate(date)
            },
            onTitleClick = {
                Log.d("homeRouter", "Title Clicked: Fetching all journals")
                viewModel.getAllJournals()
            }
        )

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = {
                Log.d("homeRouter", "Sign Out Dialog Closed")
                signOutDialogOpened = false
            },
            onYesClicked = {
                Log.d("homeRouter", "Sign Out Confirmed")
                scope.launch(Dispatchers.IO) {
                    val user = App.Companion.create(APP_ID).currentUser
                    if (user != null) {
                        Log.d("homeRouter", "Logging out user: ${user.id}")
                        user.logOut()
                        // navigate to auth screen
                        // use withContext to switch to main thread
                        withContext(Dispatchers.Main) {
                            Log.d("homeRouter", "Navigating to Auth Screen after logout")
                            navigateToAuth()
                        }
                    }
                }
            },
        )
    }
}

@SuppressLint("UnrememberedGetBackStackEntry", "UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRouter(navController: NavHostController, onBackPressed: () -> Unit) {
    Log.d("writeRouter", "Setting up Write Router")
    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val homeViewModel: HomeViewModel = viewModel(navController.getBackStackEntry(Screen.Home.route))
        //val ownerId = App.Companion.create(APP_ID).currentUser?.id
        val ownerId = AuthenticationViewModel.ownerId


        val journalId = it.arguments?.getString(WRITE_SCREEN_ARGUMENT_KEY)
        it.savedStateHandle.set(WRITE_SCREEN_ARGUMENT_KEY, journalId)

        val viewModel: WriteViewModel = viewModel(
            factory = WriteViewModelFactory(it.savedStateHandle, JournalRepository())
        )

        val selectedJournal by viewModel.selectedJournal.observeAsState()
        val galleryState = viewModel.galleryState
        val context = LocalContext.current
        val pagerState = rememberPagerState(pageCount = { Feeling.entries.size })
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        Log.d("writeRouter", "OwnerId: $ownerId, JournalId: $journalId")

        if (ownerId != null) {
            WriteScreen(
                pagerState = pagerState,
                galleryState = galleryState,
                selectedJournal = selectedJournal,
                feelingName = {
                    Feeling.entries[pageNumber].name
                },
                onTitleChanged = {
                    Log.d("writeRouter", "Title Changed: $it")
                    viewModel.setTitle(it)
                },
                onDescriptionChanged = {
                    Log.d("writeRouter", "Description Changed: $it")
                    viewModel.setDescription(it)
                },
                onBackPressed = onBackPressed,
                onDeleteConfirmed = {
                    Log.d("writeRouter", "Delete Confirmed for JournalId: $journalId")
                    viewModel.deleteJournal(journalId!!)
                    homeViewModel.refreshJournals()
                    onBackPressed()
                },
                onSaveClicked = {
                    Log.d("writeRouter", "Save Clicked for JournalId: $journalId")
                    if (journalId == null) {
                        viewModel.insertJournal(it)
                    } else {
                        viewModel.updateJournal(journalId, it)
                    }
                    homeViewModel.refreshJournals()
                    onBackPressed()
                },
                ownerId = ownerId,
                onImageSelect = {
                    val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                    Log.d("WriteViewModel", "Image Selected: Uri: $it, Type: $type")
                    viewModel.addImage(imageUri = it, imageType = type)
                }
            )
        } else {
            Log.d("writeRouter", "OwnerId is null")
        }
    }
}
