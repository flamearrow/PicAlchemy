package band.mlgb.picalchemy.utils

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Save Bitmap to internal file provider and return uri, it won't save to public gallery
 */
fun bitmapToUri(inputImage: Bitmap, context: Context): Uri {
    createInternalFileUri(context).also {
        FileOutputStream(it.absolutePath).use { fos ->
            inputImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }
        return it.contentUri
    }
}


/**
 * Convert a uri into Bitmap, the URI scheme can be 'file' for asset manager or 'content' for content provider
 */
fun uriToBitmap(inputUri: Uri, context: Context): Bitmap {
    return when (inputUri.scheme) {
        "file" -> decodeBitmapFromFileUri(inputUri, context.assets)
        "content" -> decodeBitmapFromContentUri(inputUri, context.contentResolver)
        else -> {
            throw java.lang.IllegalArgumentException("inputUri doesn't have 'file' or 'content' scheme: $inputUri")
        }
    }
}


fun decodeBitmapFromFileUri(fileUri: Uri, assetManager: AssetManager): Bitmap =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                assetManager,
                fileUri.path!!.substringAfter("/android_asset/")
            )
        ).copy(Bitmap.Config.ARGB_8888, true)
    } else {
        BitmapFactory.decodeStream(
            assetManager.open(
                fileUri.path!!.substringAfter("/android_asset/")
            )
        )
    }

fun decodeBitmapFromContentUri(inputUri: Uri, contentResolver: ContentResolver): Bitmap =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                contentResolver,
                inputUri
            )
        ).copy(Bitmap.Config.ARGB_8888, true)
    } else {
        // TODO: this might have rotation issue?
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(contentResolver, inputUri)
    }

fun scaleBitmapAndKeepRatio(
    targetBmp: Bitmap,
    reqHeightInPixels: Int,
    reqWidthInPixels: Int
): Bitmap {
    if (targetBmp.height == reqHeightInPixels && targetBmp.width == reqWidthInPixels) {
        return targetBmp
    }
    val matrix = Matrix()
    matrix.setRectToRect(
        RectF(
            0f, 0f,
            targetBmp.width.toFloat(),
            targetBmp.width.toFloat()
        ),
        RectF(
            0f, 0f,
            reqWidthInPixels.toFloat(),
            reqHeightInPixels.toFloat()
        ),
        Matrix.ScaleToFit.FILL
    )
    return Bitmap.createBitmap(
        targetBmp, 0, 0,
        targetBmp.width,
        targetBmp.width, matrix, true
    )
}

fun bitmapToByteBuffer(
    bitmapIn: Bitmap,
    width: Int,
    height: Int,
    mean: Float = 0.0f,
    std: Float = 255.0f
): ByteBuffer {
    val bitmap = scaleBitmapAndKeepRatio(bitmapIn, width, height)
    val inputImage = ByteBuffer.allocateDirect(1 * width * height * 3 * 4)
    inputImage.order(ByteOrder.nativeOrder())
    inputImage.rewind()

    val intValues = IntArray(width * height)
    bitmap.getPixels(intValues, 0, width, 0, 0, width, height)
    var pixel = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val value = intValues[pixel++]

            // Normalize channel values to [-1.0, 1.0]. This requirement varies by
            // model. For example, some models might require values to be normalized
            // to the range [0.0, 1.0] instead.
            inputImage.putFloat(((value shr 16 and 0xFF) - mean) / std)
            inputImage.putFloat(((value shr 8 and 0xFF) - mean) / std)
            inputImage.putFloat(((value and 0xFF) - mean) / std)
        }
    }

    inputImage.rewind()
    return inputImage
}

fun loadBitmapFromResources(context: Context, path: String): Bitmap {
    val inputStream = context.assets.open(path)
    return BitmapFactory.decodeStream(inputStream)
}

fun convertArrayToBitmap(
    imageArray: Array<Array<Array<FloatArray>>>,
    imageWidth: Int,
    imageHeight: Int
): Bitmap {
    val conf = Bitmap.Config.ARGB_8888 // see other conf types
    val styledImage = Bitmap.createBitmap(imageWidth, imageHeight, conf)

    for (x in imageArray[0].indices) {
        for (y in imageArray[0][0].indices) {
            val color = Color.rgb(
                ((imageArray[0][x][y][0] * 255).toInt()),
                ((imageArray[0][x][y][1] * 255).toInt()),
                (imageArray[0][x][y][2] * 255).toInt()
            )

            // this y, x is in the correct order!!!
            styledImage.setPixel(y, x, color)
        }
    }
    return styledImage
}


/**
 * Helper function used to convert an EXIF orientation enum into a transformation matrix
 * that can be applied to a bitmap.
 *
 * @param orientation - One of the constants from [ExifInterface]
 */
private fun decodeExifOrientation(orientation: Int): Matrix {
    val matrix = Matrix()

    // Apply transformation corresponding to declared EXIF orientation
    when (orientation) {
        ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_UNDEFINED -> Unit
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1F, 1F)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1F, -1F)
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.postScale(-1F, 1F)
            matrix.postRotate(270F)
        }
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.postScale(-1F, 1F)
            matrix.postRotate(90F)
        }

        // Error out if the EXIF orientation is invalid
        else -> throw IllegalArgumentException("Invalid orientation: $orientation")
    }

    // Return the resulting matrix
    return matrix
}

/**
 * sets the Exif orientation of an image.
 * this method is used to fix the exit of pictures taken by the camera
 *
 * @param filePath - The image file to change
 * @param value - the orientation of the file
 */
fun setExifOrientation(
    filePath: String,
    value: String
) {
    val exif = ExifInterface(filePath)
    exif.setAttribute(
        ExifInterface.TAG_ORIENTATION, value
    )
    exif.saveAttributes()
}

/** Transforms rotation and mirroring information into one of the [ExifInterface] constants */
fun computeExifOrientation(rotationDegrees: Int, mirrored: Boolean) = when {
    rotationDegrees == 0 && !mirrored -> ExifInterface.ORIENTATION_NORMAL
    rotationDegrees == 0 && mirrored -> ExifInterface.ORIENTATION_FLIP_HORIZONTAL
    rotationDegrees == 180 && !mirrored -> ExifInterface.ORIENTATION_ROTATE_180
    rotationDegrees == 180 && mirrored -> ExifInterface.ORIENTATION_FLIP_VERTICAL
    rotationDegrees == 270 && mirrored -> ExifInterface.ORIENTATION_TRANSVERSE
    rotationDegrees == 90 && !mirrored -> ExifInterface.ORIENTATION_ROTATE_90
    rotationDegrees == 90 && mirrored -> ExifInterface.ORIENTATION_TRANSPOSE
    rotationDegrees == 270 && mirrored -> ExifInterface.ORIENTATION_ROTATE_270
    rotationDegrees == 270 && !mirrored -> ExifInterface.ORIENTATION_TRANSVERSE
    else -> ExifInterface.ORIENTATION_UNDEFINED
}
