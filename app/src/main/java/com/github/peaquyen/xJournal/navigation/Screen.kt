package com.github.peaquyen.xJournal.navigation

import android.util.Log
import com.github.peaquyen.xJournal.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
sealed class Screen(val route: String) {
    data object Authentication : Screen( route = "authentication_screen")
    data object Home : Screen( route ="home_screen")
    data object Write : Screen(route/*key - value*/ = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}"/*the latter should be change*/) {
        fun passJournalId(journalId: String): String {
            return "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=$journalId"
        }
    }
}