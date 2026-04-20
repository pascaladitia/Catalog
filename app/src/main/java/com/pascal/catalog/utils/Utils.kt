package com.pascal.catalog.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.pascal.catalog.utils.Constant.FORMAT_DATE
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun getCurrentFormattedDate(): String {
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    return dateFormat.format(currentDate)
}

fun reFormatDate(date: String?): Pair<String?, String?> {
    if (date.isNullOrBlank()) {
        return Pair("", "")
    }

    try {
        val inputFormat = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
        val outputFormatDateMonth = SimpleDateFormat("MMM dd", Locale.getDefault())
        val outputFormatYear = SimpleDateFormat("yyyy", Locale.getDefault())

        val dateObject = inputFormat.parse(date)

        if (dateObject != null) {
            val formattedDateMonth = outputFormatDateMonth.format(dateObject)
            val formattedYear = outputFormatYear.format(dateObject)

            return Pair(formattedDateMonth, formattedYear)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return Pair("", "")
}

fun intentActionView(context: Context, url: String) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    ContextCompat.startActivity(context, i, null)
}

@Throws(IOException::class)
private fun Uri.getFileExtension(contentResolver: ContentResolver): String {
    var extension: String? = null

    if (ContentResolver.SCHEME_CONTENT == scheme && contentResolver.getType(this)?.startsWith("image/") == true) {
        val cursor: Cursor? = contentResolver.query(this, arrayOf(MediaStore.Images.ImageColumns.MIME_TYPE), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)
                if (columnIndex != -1) {
                    val mimeType = it.getString(columnIndex)
                    extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                }
            }
        }
    }

    if (extension.isNullOrEmpty()) {
        extension = MimeTypeMap.getFileExtensionFromUrl(toString())
    }

    if (extension.isNullOrEmpty()) {
        extension = "jpg"
    }

    return extension!!
}