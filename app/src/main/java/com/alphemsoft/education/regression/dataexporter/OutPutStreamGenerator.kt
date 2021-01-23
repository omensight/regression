package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.OutputStream

object OutPutStreamGenerator {
    fun generate(fileData: FileData, contentResolver: ContentResolver): OutputStream? {
        val contentValues = ContentValues()
        val fileName = fileData.fileName
        if (fileName.isEmpty()){
            return null
        }
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileData.commonExtension)
        contentValues.apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$fileName.${fileData.commonExtension}")
            put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }
        val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        return uri?.let { contentResolver.openOutputStream(it) }
    }

}
