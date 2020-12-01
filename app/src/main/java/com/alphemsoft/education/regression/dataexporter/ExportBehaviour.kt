package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import com.alphemsoft.education.regression.data.model.SheetEntry

abstract class ExportBehaviour(
    protected val contentValues: ContentValues,
    protected val contentResolver: ContentResolver
) {
    abstract fun export(data: List<SheetEntry>): Boolean
}