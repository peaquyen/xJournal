package com.github.peaquyen.xJournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

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

fun NavGraphBuilder.authenticationRouter() {
    composable(route = Screen/*class*/.Authentication/*object*/.route) {
        //define the actual screen
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