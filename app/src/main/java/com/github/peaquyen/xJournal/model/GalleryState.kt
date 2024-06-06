package com.github.peaquyen.xJournal.model

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun rememberGalleryState(): GalleryState {
    return remember { GalleryState() }
}

class GalleryState {
    val images = mutableStateListOf<GalleryImage>()
    private val imagesToBeDeleted = mutableStateListOf<GalleryImage>()

    fun addImage(galleryImage: GalleryImage) {
        images.add(galleryImage)
    }

    fun removeImage(galleryImage: GalleryImage) {
        images.remove(galleryImage)
        imagesToBeDeleted.add(galleryImage)
    }

}

/**
 * GalleryImage is a data class that holds the image and the remote path of the image.
 * @param image: the image uri
 * @param remoteImagePath: the remote path of the image
 */
data class GalleryImage(
    val image: Uri,
    val remoteImagePath: String = "",
)