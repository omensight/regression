package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream

object OutPutStreamGenerator {
    fun generate(fileData: DataExportHelper.FileData, contentResolver: ContentResolver): OutputStream? {
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "${fileData.fileName}.${fileData.commonExtension}")
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileData.commonMimeType)
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }
        val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        return uri?.let { contentResolver.openOutputStream(it) }
    }

}
