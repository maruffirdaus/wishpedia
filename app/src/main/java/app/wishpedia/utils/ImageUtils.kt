package app.wishpedia.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

object ImageUtils {
    fun storeImage(context: Context, uri: Uri): Uri {
        val bitmap = getBitmapFromUri(context, uri)
        val bytes = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val file = File(context.getExternalFilesDir(null), "${LocalDateTime.now()}.jpg")

        file.createNewFile()

        val fileOutputStream = FileOutputStream(file)

        fileOutputStream.write(bytes.toByteArray())
        fileOutputStream.close()

        bitmap.recycle()

        return file.toUri()
    }

    private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)

        parcelFileDescriptor?.close()

        return image
    }
}