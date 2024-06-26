package com.github.peaquyen.xJournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.github.peaquyen.xJournal.data.repository.MongoDB
import com.github.peaquyen.xJournal.navigation.Screen
import com.github.peaquyen.xJournal.navigation.SetUpNavGraph
import com.github.peaquyen.xJournal.ui.theme.xJournalTheme
import com.github.peaquyen.xJournal.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        // Transparent status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        MongoDB.configureTheRealm()

        setContent {
            xJournalTheme {
                val navController = rememberNavController()
                SetUpNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController
                )
            }
        }
    }
}

private fun getStartDestination(): String /*return router of our definite destination*/ {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) {
        Screen.Home.route
    } else {
        Screen.Authentication.route
    }
}















