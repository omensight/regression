package com.alphemsoft.education.regression.dataexporter.exportbehaviour

import android.content.Context
import android.net.Uri
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.dataexporter.FileData
import com.alphemsoft.education.regression.dataexporter.OutPutStreamGenerator
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

abstract class ExportBehaviour(val uri: Uri) {

    abstract fun export(data: List<SheetEntry>): Boolean

    class Builder(
        private val context: Context,
        private val fileData: FileData
    ) {

        fun build(): ExportBehaviour? {
            val contentResolver = context.contentResolver
            val outPutStreamGenerator = OutPutStreamGenerator()
            val uri: Uri? = outPutStreamGenerator.getUri(
                fileData.fileName,
                fileData.commonExtension,
                contentResolver
            )
            val outputStream: OutputStream? = uri?.let { contentResolver.openOutputStream(uri) }
            return outputStream?.let {
                when (fileData) {
                    is FileData.Csv -> {
                        CsvExportBehaviour(outputStream, uri)
                    }
                    is FileData.Excel -> {
                        XlsxExportBehaviour(XSSFWorkbook(), outputStream, uri)
                    }
                }
            }
        }
    }

}

