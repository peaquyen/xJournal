package com.github.peaquyen.xJournal.model

// data structure
data class Journal(
    var id: String, // convert API Value:  INT -> String
    var ownerId: String,
    var feeling: String,
    var title: String,
    var description: String,
    var images: List<String>,
    var date: String
){
    companion object {
        fun createNew(ownerId: String, feeling: String, title: String, description: String, images: List<String>, date: String): Journal {
            return Journal(id = "", ownerId = ownerId, feeling = feeling, title = title, description = description, images = images, date = date)
        }
    }
}