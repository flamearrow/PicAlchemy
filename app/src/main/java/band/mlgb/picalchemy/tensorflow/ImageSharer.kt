package band.mlgb.picalchemy.tensorflow

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface ImageSharer {
    suspend fun share(imageUri: Uri)
}


@Singleton
class IntentImageSharer @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageSharer {
    override suspend fun share(imageUri: Uri) {
        context.startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}