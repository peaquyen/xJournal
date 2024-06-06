package com.github.peaquyen.xJournal.presentation.screens.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.peaquyen.xJournal.R
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.model.RequestState
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    journals: RequestState<Map<LocalDate, List<Journal>>>,
    drawerState: DrawerState,
    onSignOutClick: () -> Unit,
    onMenuClick: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    context: Context,
    filteredJournals: List<Journal>?,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onTitleClick : () -> Unit
) {
    Log.d("NavGraph", journals.toString())
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val onCalendarClick: () -> Unit = {
        val calendar = Calendar.getInstance()
        selectedDate?.let { date ->
            calendar.set(date.year, date.monthValue - 1, date.dayOfMonth)
        }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClick = onSignOutClick,
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(
                    scrollBehavior = scrollBehavior,
                    onMenuClick = onMenuClick,
                    onCalendarClick = onCalendarClick,
                    onTitleClick = onTitleClick,
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = navigateToWrite
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New xJournal Icon"
                    )
                }
            },
            content = {
                when (journals) {
                    is RequestState.Loading -> {
                        // Display a loading indicator
                    }
                    is RequestState.Error -> {
                        EmptyPage(
                            title = "Error",
                            subtitle = "Error: ${journals.exception.message}",
                        )
                    }
                    is RequestState.Success -> {
                        val journalNotes = (journals as RequestState.Success).data
                        HomeContent(
                            paddingValues = it,
                            journalNotes = journalNotes,
                            onClick = navigateToWriteWithArgs,
                            filteredJournals = filteredJournals
                        )
                    }
                    RequestState.Idle -> TODO()
                }
            }
        )
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // add a drawer sheet to the drawer content
          ModalDrawerSheet(
              content = {
                  Box(
                      modifier = Modifier
                          .fillMaxWidth()
                          .height(250.dp)
                  ) {
                      Image(
                          modifier = Modifier
                              .size(250.dp)
                              .align(Alignment.Center),
                          painter = painterResource(id = R.drawable.logo),
                          contentDescription = "Logo Image",

                          )
                  }
                  NavigationDrawerItem(
                      label = {
                          Row(
                              modifier = Modifier
                                  .padding(horizontal = 12.dp)
                          ) {
                              Icon(
                                  painter = painterResource(id = R.drawable.google_logo),
                                  contentDescription = "Google Logo",
                                  tint = MaterialTheme.colorScheme.onSurface
                              )
                              Spacer(modifier = Modifier.width(12.dp))
                              Text(
                                  "Sign Out",
                                  color = MaterialTheme.colorScheme.onSurface
                              )
                          }
                      },
                      selected = false,
                      onClick = onSignOutClick
                  )
              }
          )
        },
        content = content
    )
}