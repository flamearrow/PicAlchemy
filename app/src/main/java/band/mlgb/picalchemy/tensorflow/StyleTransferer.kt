package band.mlgb.picalchemy.tensorflow

import android.content.Context
import android.net.Uri
import band.mlgb.picalchemy.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import javax.inject.Inject

/**
 * Execute the style transferring tensorflow model with a dedicated single thread dispatcher
 * https://tfhub.dev/google/magenta/arbitrary-image-stylization-v1-256/2
 */
class StyleTransferer @Inject constructor(
    @ApplicationContext context: Context,
    private val executorCoroutineDispatcher: ExecutorCoroutineDispatcher
) {

    private val interpreterPredict: Interpreter
    private val interpreterTransform: Interpreter

    init {
        interpreterPredict = getInterpreter(context, STYLE_PREDICT_FP16_MODEL)
        interpreterTransform = getInterpreter(context, STYLE_TRANSFER_FP16_MODEL)
    }

    companion object {
        private const val STYLE_IMAGE_SIZE = 256
        private const val CONTENT_IMAGE_SIZE = 384
        private const val BOTTLENECK_SIZE = 100
        private const val STYLE_PREDICT_FP16_MODEL =
            "arbitrary-image-stylization-v1-tflite-256-fp16-prediction.tflite"
        private const val STYLE_TRANSFER_FP16_MODEL =
            "arbitrary-image-stylization-v1-tflite-256-fp16-transfer.tflite"
    }

    suspend fun transferStyle(
        styleUri: Uri,
        inputUri: Uri,
        context: Context
    ): Uri? =
        withContext(executorCoroutineDispatcher) {
            try {
                val contentImage = uriToBitmap(inputUri, context)
                val contentArray =
                    bitmapToByteBuffer(contentImage, CONTENT_IMAGE_SIZE, CONTENT_IMAGE_SIZE)
                val styleBitmap = uriToBitmap(styleUri, context)
                val input =
                    bitmapToByteBuffer(styleBitmap, STYLE_IMAGE_SIZE, STYLE_IMAGE_SIZE)

                val inputsForPredict = arrayOf<Any>(input)
                val outputsForPredict = HashMap<Int, Any>()
                val styleBottleneck =
                    Array(1) { Array(1) { Array(1) { FloatArray(BOTTLENECK_SIZE) } } }
                outputsForPredict[0] = styleBottleneck
                // The results of this inference could be reused given the style does not change
                // That would be a good practice in case this was applied to a video stream.
                interpreterPredict.runForMultipleInputsOutputs(inputsForPredict, outputsForPredict)

                val inputsForStyleTransfer = arrayOf(contentArray, styleBottleneck)
                val outputsForStyleTransfer = HashMap<Int, Any>()
                val outputImage =
                    Array(1) { Array(CONTENT_IMAGE_SIZE) { Array(CONTENT_IMAGE_SIZE) { FloatArray(3) } } }
                outputsForStyleTransfer[0] = outputImage

                interpreterTransform.runForMultipleInputsOutputs(
                    inputsForStyleTransfer,
                    outputsForStyleTransfer
                )
                bitmapToUri(
                    convertArrayToBitmap(
                        outputImage,
                        CONTENT_IMAGE_SIZE,
                        CONTENT_IMAGE_SIZE
                    ), context
                )
            } catch (e: Exception) {
                errBGLM("something went wrong when running style transfer: ${e.message}")
                null
            }
        }

    protected fun finalize() {
        interpreterPredict.close()
        interpreterTransform.close()
    }
}