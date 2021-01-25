package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object OutPutStreamGenerator {

    fun getUri(fileName: String, extension: String, contentResolver: ContentResolver): Uri? {
        val contentValues = ContentValues()
        var uri: Uri? = null
        if (fileName.isNotEmpty()){
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            contentValues.apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$fileName.${extension}")
                put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }
            uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        }
        return uri
    }

}
