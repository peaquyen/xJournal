package com.github.peaquyen.xJournal.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.presentation.components.JournalHolder
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    journalNotes: Map<LocalDate, List<Journal>>,
    onClick: (String) -> Unit
) {
    if (journalNotes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            journalNotes.forEach { (localDate, journals) ->
                // stickyHeader is a sticky header that will stick to the top of the list
                stickyHeader(key = localDate) {
                    DateHeader(localDate)
                }

                items(
                    items = journals,
                    key = { it._id.toString() }
                ) {
                    JournalHolder(Journal = it, onClick = onClick)
                }
            }
        }
    } else {
        EmptyPage()
    }
}

@Composable
fun DateHeader(LocalDate: LocalDate) {
    // A composable function that displays the date of the diary entry

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End){
            Text(
                text = String.format("%02d", LocalDate.dayOfMonth),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = LocalDate.dayOfWeek.toString().take(3),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(horizontalAlignment = Alignment.Start){
            Text(
                text = LocalDate.month.toString().lowercase()
                    .replaceFirstChar { it.uppercase()},
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = "${LocalDate.year}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }
}


@Composable
fun EmptyPage(
    title: String = "Empty Diary",
    subtitle: String = "Write Something"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Composable
@Preview
fun DateHeaderPreview() {
    DateHeader(LocalDate.now())
}