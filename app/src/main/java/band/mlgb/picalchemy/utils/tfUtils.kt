package band.mlgb.picalchemy.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


@Throws(IOException::class)
fun getInterpreter(
    context: Context,
    modelName: String
): Interpreter {
    val tfliteOptions = Interpreter.Options().also { it.setNumThreads(4) }
    return Interpreter(loadModelFile(context, modelName), tfliteOptions)
}

@Throws(IOException::class)
fun loadModelFile(context: Context, modelFile: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelFile)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    val retFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    fileDescriptor.close()
    return retFile
}
