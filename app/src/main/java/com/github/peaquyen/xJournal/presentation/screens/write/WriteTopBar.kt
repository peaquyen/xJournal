package com.github.peaquyen.xJournal.presentation.screens.write

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.presentation.components.DisplayAlertDialog
import com.github.peaquyen.xJournal.util.convertStringToInstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedJournal: Journal?,
    feelingName: () -> String,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
) {
//    val currentDate by remember { mutableStateOf(LocalDate.now()) }
//    val currentTime = remember { mutableStateOf(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))) }
//    val formattedDate = remember(currentDate) {
//        DateTimeFormatter.ofPattern("dd MMM yyyy").format(currentDate).uppercase()
//    }
//    val formattedTime = remember(currentTime) {
//        DateTimeFormatter.ofPattern("hh:mm a").format(currentTime.value).uppercase()
//    }

    val selectedJournalDateTime = remember(selectedJournal) {
        if (selectedJournal?.date != null) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            dateFormat.format(Date.from(selectedJournal.date.convertStringToInstant())).uppercase()
        } else {
            "Unknown"
        }
    }

    Log.d("WriteScreen", "selectedJournalDateTime: $selectedJournalDateTime")

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Erro Icon"
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = feelingName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center

                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = selectedJournalDateTime,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                    ),
                    textAlign = TextAlign.Center
                )
            }
        },
        actions = {
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Data icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            if (selectedJournal != null) {
                DeleteDiaryAction(selectedJournal, onDeleteConfirmed = onDeleteConfirmed)
            }
        }
    )
}

@Composable
fun DeleteDiaryAction(
    selectedDiary: Journal?,
    onDeleteConfirmed: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Delete")
            }, onClick = {
                openDialog.value = true
                expanded.value = false
            }
        )
    }
    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to permanently delete this diary note '${selectedDiary?.title}'?",
        dialogOpened = openDialog.value,
        onDialogClosed = { openDialog.value = false },
        onYesClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded.value = !expanded.value }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}