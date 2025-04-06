package band.mlgb.picalchemy.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

data class ContentUriResult(val contentUri: Uri, val absolutePath: String)

/**
 * Create a file in app's external files Dir and return the uri
 */
fun createInternalFileUri(context: Context): ContentUriResult {
    createImageFile(context).also { file ->
        return ContentUriResult(
            FileProvider.getUriForFile(
                context,
                "band.mlgb.picalchemy.fileprovider",
                file
            ), file.absolutePath
        )
    }

}

@Throws(IOException::class)
private fun createImageFile(context: Context): File {
    return File.createTempFile(
        "JPEG_${generateJpgFileName()}_", /* prefix */
        ".jpg", /* suffix */
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        // /storage/emulated/0/Android/data/band.mlgb.picalchemy/files/Pictures/JPEG_20201111_182533_7577798496808625055.jpg
    )
}

fun generateJpgFileName() =
    "JPEG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())


/**
 * Decode uri into a bitmap and save it to gallery, return success or not
 */
suspend fun saveUriToGallery(srcUri: Uri, context: Context): Boolean =
    withContext(Dispatchers.IO) {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, generateJpgFileName())
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )?.let { insertedUri ->
                val bitmap = uriToBitmap(srcUri, context)
                context.contentResolver.openOutputStream(insertedUri)?.use { os ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    context.contentResolver.update(insertedUri, contentValues, null, null)
                }
            }
            true
        } catch (e: Exception) {
            errBGLM("Error saving file: $e")
            false
        }
    }