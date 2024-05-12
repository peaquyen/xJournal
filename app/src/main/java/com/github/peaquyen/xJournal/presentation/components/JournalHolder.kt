package com.github.peaquyen.xJournal.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.peaquyen.xJournal.model.Feeling
import com.github.peaquyen.xJournal.model.Journal
import com.github.peaquyen.xJournal.ui.theme.Elevation
import com.github.peaquyen.xJournal.util.toInstant
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O) //the annotated method or class should only be
// used on Android devices running API level 26 (Android 8.0, codename Oreo) or higher.
@Composable
fun JournalHolder(
    journal: Journal,
    onClick: (String) -> Unit /*object ID of each journal*/
) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var galleryOpened by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable (
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {onClick(journal._id.toString())}
    ) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(20.dp))

        // box of content
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) {
                        it.size.height.toDp()
                    }
                },
            tonalElevation = Elevation.Level1
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ){
                JournalHeader(journal.feeling, journal.date.toInstant())
                Text(
                    modifier = Modifier.padding(14.dp),
                    text = journal.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                if (journal.images.isNotEmpty()){
                    ShowGalleryButton(
                        galleryOpened = galleryOpened,
                        onClick = {
                            galleryOpened = !galleryOpened
                        }
                    )
                }
                // NOTE: remember this carefully
                AnimatedVisibility(visible = galleryOpened) {
                    Column(modifier = Modifier.padding(all = 14.dp)) {
                        Gallery(images = journal.images)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalHeader(feelingName: String, time: Instant) {
    val feeling by remember { mutableStateOf(Feeling.valueOf(feelingName)) }
    val formatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(feeling.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = feeling.icon),
                contentDescription = "feeling Icon",
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = feeling.name,
                color = feeling.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = formatter.format(time),
            color = feeling.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun JournalHolderPreview() {
    JournalHolder(
            journal = Journal().apply {
            title = "Title"
            description = "Description"
            feeling = "HAPPY"
        },
        onClick = {}
    )
}


