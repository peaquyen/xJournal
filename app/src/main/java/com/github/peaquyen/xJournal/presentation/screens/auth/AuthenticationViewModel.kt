package com.github.peaquyen.xJournal.presentation.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.peaquyen.xJournal.util.Constants.APP_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
class AuthenticationViewModel : ViewModel() {
    private val TAG = "AuthenticationViewModel"

    var authenticated = mutableStateOf(false)
        private set
    var loadingGoogleState = mutableStateOf(false)
        private set
    var loadingEmailState = mutableStateOf(false)
        private set
    var loadingCreateAccount = mutableStateOf(false)
        private set

    //public lateinit var ownerId: String

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    //val uid = auth.currentUser?.uid


    // Fix the getUid method
    public fun getUid(): String? {
        return auth.currentUser?.uid
    }

    fun setGoogleLoading(loading: Boolean) {
        loadingGoogleState.value = loading
    }

    fun setEmailLoading(loading: Boolean) {
        loadingEmailState.value = loading
    }

    fun setCreateAccountLoading(loading: Boolean) {
        loadingCreateAccount.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            setGoogleLoading(true)
            Log.d("Auth", "Token: $tokenId")
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.google(tokenId, GoogleAuthType.ID_TOKEN)
                    ).loggedIn
                }
                Log.d("Auth", "Result: $result")
                withContext(Dispatchers.Main) {
                    setGoogleLoading(false)
                    if (result) {
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                        Log.d("Auth", "Authenticated")
                    } else {
                        onError(Exception("User is not logged in."))
                    }
                }
            } catch (e: Exception) {
                setGoogleLoading(false)
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
        //ownerId = App.Companion.create(APP_ID).currentUser?.id!!
    }

    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        Log.d(TAG, "signInWithEmail: started with email: $email")
        //Log.d(TAG, "signInWithEmail: started with uid: $uid")
        setEmailLoading(true)
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                Log.d(TAG, "signInWithEmail: successfully signed in with email")
                onSuccess()
                authenticated.value = true
            } catch (e: Exception) {
                Log.e(TAG, "signInWithEmail: error signing in with email", e)
                onError(e)
            } finally {
                setEmailLoading(false)
            }
        }
        //ownerId = AuthenticationViewModel().getUid()!!
    }

    fun createAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        Log.d(TAG, "createAccount: started with email: $email")
        setCreateAccountLoading(true)
        viewModelScope.launch {
            runCatching {
                Log.d(TAG, "createAccount: creating account with email and password")
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                Log.d(TAG, "createAccount: account created with user: ${user?.uid}")

                user?.let {
                    sendEmailVerification(it)
                    Log.d(TAG, "createAccount: email verification sent")
                    onSuccess()
                } ?: throw Exception("User creation failed, user is null")
            }.onFailure {
                Log.e(TAG, "createAccount: error creating account", it)
                onError(it as Exception)
            }.onSuccess {
                setCreateAccountLoading(false)
            }
        }
    }

    private suspend fun sendEmailVerification(user: FirebaseUser) {
        try {
            user.sendEmailVerification().await()
            Log.d(TAG, "sendEmailVerification: email verification sent to ${user.email}")
        } catch (e: Exception) {
            Log.e(TAG, "sendEmailVerification: failed to send email verification", e)
            throw e
        }
    }

    companion object {
        var ownerId: String = AuthenticationViewModel.ownerId
    }
}