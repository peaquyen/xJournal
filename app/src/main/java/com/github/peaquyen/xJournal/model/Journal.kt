package com.github.peaquyen.xJournal.model

import java.time.LocalDate

// data structure
data class Journal(
    val id: Int,
    val ownerId: String,
    val feeling: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val date: String
)