package com.alphemsoft.education.regression.dataexporter

import com.alphemsoft.education.regression.data.model.SheetEntry

class DataExportHelper {

    var exportBehaviour: ExportBehaviour? = null

    fun export(data: List<SheetEntry>): Boolean {
        require(exportBehaviour != null) { "Export behaviour not set" }
        return exportBehaviour?.export(data) ?: false
    }

    sealed class FileData(
        val fileName: String,
        val commonMimeType: String,
        val commonExtension: String
    ) {
        class Csv(fileName: String): FileData(fileName, "text/*", "csv")
    }
}