package band.mlgb.picalchemy.utils

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.Channel
import timber.log.Timber
import javax.inject.Inject


/**
 * TODO: Consider using CameraX
 */
@ActivityScoped
class ActivityPhotoTaker @Inject constructor(
    @ActivityContext private val context: Context
) : PhotoTaker {
    private val takePictureLauncher: ActivityResultLauncher<Uri>
    private val photoChannel: Channel<TakePhotoState> = Channel()
    private var currentContent: ContentUriResult? = null

    init {
        takePictureLauncher = (context as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.TakePicture(),
        ) { success ->
            if (success) {
                Timber.d("Take picture result: $success")
                currentContent?.let {
                    photoChannel.trySend(TakePhotoState.Success(it.contentUri))
                } ?: run {
                    photoChannel.trySend(TakePhotoState.Error(IllegalStateException("currentContent is null")))
                }
            } else {
                photoChannel.trySend(TakePhotoState.Cancelled)
            }
        }
    }

    override suspend fun takePhoto(): TakePhotoState {
        currentContent = createInternalFileUri(context)
        takePictureLauncher.launch(requireNotNull(currentContent).contentUri)

        runCatching { photoChannel.receive() }.fold(
            onSuccess = {
                return it
            },
            onFailure = {
                return TakePhotoState.Error(IllegalStateException("Take Photo Failed"))
            }
        )
    }
}