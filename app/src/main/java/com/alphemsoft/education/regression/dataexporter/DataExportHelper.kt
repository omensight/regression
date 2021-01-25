package com.alphemsoft.education.regression.dataexporter

import android.net.Uri
import com.alphemsoft.education.regression.data.model.SheetEntry

class DataExportHelper {

    var uri: Uri? = null
        private set

    var exportBehaviour: ExportBehaviour? = null
        set(value) {
            value?.uri?.let {uri->
                this.uri = uri
            }
            field = value
        }

    fun export(data: List<SheetEntry>): Boolean {
        require(exportBehaviour != null) { "Export behaviour not set" }
        return exportBehaviour?.export(data) ?: false
    }
}