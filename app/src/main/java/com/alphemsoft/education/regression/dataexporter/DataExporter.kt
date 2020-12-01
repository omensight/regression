package com.alphemsoft.education.regression.dataexporter

import com.alphemsoft.education.regression.data.model.SheetEntry

class DataExporter{
    var exportBehaviour: ExportBehaviour? = null
    fun export(data: List<SheetEntry>): Boolean {
        return exportBehaviour?.export(data) ?: false
    }
}