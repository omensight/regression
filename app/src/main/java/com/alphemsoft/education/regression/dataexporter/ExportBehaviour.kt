package com.alphemsoft.education.regression.dataexporter

import android.content.Context
import com.alphemsoft.education.regression.data.model.SheetEntry
import java.io.OutputStream

interface ExportBehaviour {

    abstract fun export(data: List<SheetEntry>): Boolean

    class Builder(
        private val context: Context,
        private val fileData: DataExportHelper.FileData
    ) {
        fun build(): ExportBehaviour {
            val contentResolver = context.contentResolver
            val outputStream = OutPutStreamGenerator.generate(fileData, contentResolver)
            return when (fileData) {
                is DataExportHelper.FileData.Csv -> {
                    CsvExportBehaviour(outputStream!!)
                }
            }
        }
    }

}

