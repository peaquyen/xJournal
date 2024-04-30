package com.github.peaquyen.xJournal.presentation.screens.home

import android.annotation.SuppressLint
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.peaquyen.xJournal.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    onSignOutClick: () -> Unit,
    onMenuClick: () -> Unit,
    navigateToWrite: () -> Unit
) {
   NavigationDrawer(
         drawerState = drawerState,
         onSignOutClick = onSignOutClick,
   ) {
       Scaffold(
           topBar = {
               HomeTopBar(
                   onMenuClick = onMenuClick
               )
           },

           // floating action button to navigate to write screen
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