package band.mlgb.picalchemy.utils

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf


val LocalPhotoTaker = compositionLocalOf<PhotoTaker> { PhotoTaker.Dummy }


sealed interface TakePhotoState {
    data object Cancelled : TakePhotoState
    data class Error(val error: Throwable) : TakePhotoState
    data class Success(val uri: Uri) : TakePhotoState
}

/**
 * Take photo and returns a [Uri] to the photo.
 */
interface PhotoTaker {
    suspend fun takePhoto(): TakePhotoState

    object Dummy : PhotoTaker {
        override suspend fun takePhoto(): TakePhotoState = TakePhotoState.Success(Uri.EMPTY)
    }
}
