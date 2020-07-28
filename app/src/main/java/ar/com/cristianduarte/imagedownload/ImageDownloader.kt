package ar.com.cristianduarte.imagedownload

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*


class ImageDownloader {

    suspend fun saveImageToGallery(context: Context, imgUrl: String): Boolean {
        return try {
            saveImageToGalleryInternal(context, imgUrl)
        } catch (e: Exception) {
            Log.e(javaClass.canonicalName, "error saving image $imgUrl", e)
            false
        }
    }

    private suspend fun saveImageToGalleryInternal(context: Context, imgUrl: String): Boolean {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        val sourceFile: File?

        withContext(Dispatchers.IO) {
            sourceFile = Glide.with(context).asFile().load(imgUri).submit().get()
        }

        if (sourceFile == null) {
            return false
        }

        //val sourcePath: String = sourceFile.path

        val name = imgUri.lastPathSegment
        val fos: OutputStream?
        var destImageHandle: File? = null // only when saving image witout MediaStore

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "name")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + context.applicationInfo.loadLabel(context.packageManager).toString()
            )
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        } else {

            val imagesDir: File? =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    File.separator + context.applicationInfo.loadLabel(context.packageManager).toString()
                )
            if (imagesDir != null) {
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }
            } else {
                return false
            }
            destImageHandle = File(imagesDir, name)
            fos = FileOutputStream(destImageHandle)
        }

        if (fos == null) {
            return false
        }

        withContext(Dispatchers.IO) {
            val bis = BufferedInputStream(sourceFile.inputStream())
            bis.copyTo(fos)
            fos.close()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // not needed
            } else {
                if (destImageHandle != null) {
                    MediaScannerConnection.scanFile(context, listOf(destImageHandle.absolutePath).toTypedArray(), listOf("image/jpeg").toTypedArray(), null)
                }
            }
        }
        return true
    }
}