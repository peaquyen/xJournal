package com.github.peaquyen.xJournal.presentation.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.util.Constants.APP_ID
import io.reactivex.internal.operators.single.SingleDoOnSuccess
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class AuthenticationViewModel: ViewModel() {
    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    suspend fun signInWithMongoAtlas(
        // suspend is added
        tokenId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
        // When you launch a coroutine within a specific CoroutineScope,
        // you're tying the lifecycle of that coroutine to the lifecycle
        // of that scope. If the scope gets cancelled, all coroutines
        // launched within that scope will be cancelled as well.
        }
            try {
                //A Dispatcher in Kotlin Coroutines is a component that determines which
                // thread a coroutine should run on. It can be thought of as a thread pool
                // manager for coroutines.
                val result = withContext(Dispatchers.IO) /*meaning we are using MongoDB :v */{
                    // Dispatchers.IO: This dispatcher is optimized for offloading blocking IO
                    // tasks to a shared pool of threads.
                    // It's typically used for tasks like network calls or disk reads/writes.
                    App.Companion.create(APP_ID).login(
                        Credentials.google(tokenId, GoogleAuthType.ID_TOKEN)
                    ).loggedIn //return boolean for account made or not
                }
                withContext(Dispatchers.Main) {
                    // Dispatchers.Main: This dispatcher confines(gioi han) the coroutine to the main thread.
                    // It's typically used for UI-related tasks in Android.
                    onSuccess(result)
                }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
            }
        }
    }
}