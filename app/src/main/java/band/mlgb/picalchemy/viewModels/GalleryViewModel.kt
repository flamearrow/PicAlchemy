package band.mlgb.picalchemy.viewModels

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import band.mlgb.picalchemy.utils.errBGLM
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// ViewModel using ContentProvider to access local images
// use a suspend function to load from content provider and cursor
@FragmentScoped
class GalleryViewModel @Inject constructor(private val contentResolver: ContentResolver) :
    ViewModel() {
    val imageUris: LiveData<List<Uri>> = liveData {
        emit(loadImages())
    }


    // IO intensive function, to be called inside a coroutine
    // query the MediaStore, find all images with type image type and return them as a list of uris
    private fun fetchImages(): List<Uri> {
        contentResolver.query(
            // this only returns image, if need video, add
            // MediaStore.Files
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID, // used to build uri
                MediaStore.Images.Media.DISPLAY_NAME, // should be used to decide type
                MediaStore.MediaColumns.MIME_TYPE
            ),
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED
        )?.let { cursor ->
            return mutableListOf<Uri>().apply {
                if (cursor.moveToLast()) {
                    while (!cursor.isBeforeFirst) {
                        add(
                            ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                cursor.getLong((cursor.getColumnIndex(MediaStore.Images.Media._ID)))
                            )
                        )
                        cursor.moveToPrevious()
                    }
                }
                cursor.close()
            }
        } ?: run {
            errBGLM("failed to pull images, return nothing")
            return listOf()
        }

    }

    private suspend fun loadImages() =
        withContext(Dispatchers.IO) {
            val images = fetchImages()
            images
        }

    companion object {
        fun providerFactory(
            contentResolver: ContentResolver
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GalleryViewModel(contentResolver) as T
            }
        }

    }
}