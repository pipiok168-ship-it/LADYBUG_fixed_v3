package com.secondhand.vip.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object PathUtil {

    fun getPath(context: Context, uri: Uri): String {

        var result: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor? =
            context.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null) {
            val columnIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }

        return result ?: ""
    }
}
