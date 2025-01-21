package band.mlgb.picalchemy.viewModels

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.*
import band.mlgb.picalchemy.utils.errBGLM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToyViewModel(app: Application) : AndroidViewModel(app) {
    val imageUris: LiveData<List<Uri>> = liveData {
        emit(loadImages())
    }


    // IO intensive function, to be called inside a coroutine
    // query the MediaStore, find all images with type image type and return them as a list of uris
    private fun fetchImages(): List<Uri> {

        getApplication<Application>().contentResolver.query(
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
            app: Application
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GalleryViewModel(app.contentResolver) as T
            }

        }

    }
}