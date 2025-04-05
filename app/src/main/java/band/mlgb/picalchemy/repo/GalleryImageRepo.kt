package band.mlgb.picalchemy.repo

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GalleryImageRepo @Inject constructor(
    val contentResolver: ContentResolver
) {
    val galleryImages: Flow<List<Uri>> = observeImages()


    /**
     * Observer any changes in contentResolver and returns the [MediaStore.Images.Media.EXTERNAL_CONTENT_URI]
     */
    private fun observeImages(): Flow<List<Uri>> = callbackFlow {
        // Helper function to query images from MediaStore
        suspend fun queryImages(): List<Uri> = withContext(Dispatchers.IO) {
            val uris = mutableListOf<Uri>()
            val projection = arrayOf(MediaStore.Images.Media._ID)
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                    uris.add(contentUri)
                }
            }
            uris
        }

        // Create a ContentObserver to listen for changes in the MediaStore
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                // When the content changes, query the images and send the new list
                // We launch in the callbackFlow's scope to avoid blocking the observer callback.
                launch {
                    trySend(queryImages())
                }
            }
        }

        // Register the observer on the MediaStore Images URI
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, observer
        )

        // Emit the initial list of images
        trySend(queryImages())

        // Remove the observer when the flow collection is cancelled
        awaitClose {
            contentResolver.unregisterContentObserver(observer)
        }
    }

}