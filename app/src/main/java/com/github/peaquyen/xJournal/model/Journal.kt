package com.github.peaquyen.xJournal.model

import java.time.LocalDate

// data structure
data class Journal(
    val id: String, // convert API Value:  INT -> String
    val ownerId: String,
    var feeling: String,
    var title: String,
    var description: String,
    val images: List<String>,
    val date: String
)