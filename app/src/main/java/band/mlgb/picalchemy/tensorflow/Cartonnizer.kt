package band.mlgb.picalchemy.tensorflow

import android.content.Context
import android.net.Uri
import band.mlgb.picalchemy.ml.WhiteboxCartoonGanInt8
import band.mlgb.picalchemy.utils.bitmapToUri
import band.mlgb.picalchemy.utils.errBGLM
import band.mlgb.picalchemy.utils.scaleBitmapAndKeepRatio
import band.mlgb.picalchemy.utils.uriToBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.image.TensorImage
import javax.inject.Inject

/**
 * Run CartoonGan model
 * https://tfhub.dev/sayakpaul/lite-model/cartoongan/dr/1
 *
 * Uses TFLite task support library's [TensorImage]
 */

class Cartonnizer @Inject constructor(
    @ApplicationContext val context: Context,
    private val executorCoroutineDispatcher: ExecutorCoroutineDispatcher
) {

    var cartoonizer: WhiteboxCartoonGanInt8 = WhiteboxCartoonGanInt8.newInstance(context)

    suspend fun cartoonize(
        inputUri: Uri
    ): Uri? =
        withContext(executorCoroutineDispatcher) {
            try {
                val inputImage = TensorImage.fromBitmap(
                    scaleBitmapAndKeepRatio(
                        uriToBitmap(inputUri, context),
                        INPUT_SIZE,
                        INPUT_SIZE
                    )
                )
                val outputImage =
                    cartoonizer.process(inputImage).cartoonizedImageAsTensorImage.bitmap
                bitmapToUri(outputImage, context)
            } catch (e: Exception) {
                errBGLM("something went wrong when running catonnization: ${e.message}")
                null
            }
        }

    companion object {
        private const val INPUT_SIZE = 512
    }

    protected fun finalize() {
        cartoonizer.close()
    }

}