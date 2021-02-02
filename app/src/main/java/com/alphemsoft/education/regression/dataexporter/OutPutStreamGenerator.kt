package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File

class OutPutStreamGenerator {

    fun getUri(fileName: String, extension: String, contentResolver: ContentResolver): Uri? {
        val contentValues = ContentValues()
        var uri: Uri? = null
        if (fileName.isNotEmpty()){
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val completeFileName = "$fileName.${extension}"
            contentValues.apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, completeFileName)
                put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            }else{
                val root = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                if (!root.exists()){
                    root.mkdir()
                }
                val file = File(root, fileName)
                file.createNewFile()
                Uri.fromFile(file)
            }
        }
        return uri
    }

}
