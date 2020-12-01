package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.MediaStore
import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter

class CsvExportBehaviour constructor(
    contentValues: ContentValues,
    contentResolver: ContentResolver
) :
    ExportBehaviour(contentValues, contentResolver) {

    private var outputStream: OutputStream? = null

    private var csvPrinter: CSVPrinter? = null

    override fun export(data: List<SheetEntry>): Boolean {
        val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues) ?: return false
        outputStream = contentResolver.openOutputStream(uri)
        csvPrinter = CSVPrinter(OutputStreamWriter(outputStream), CSVFormat.EXCEL)
        var result = true
        data.let { safeData ->
            for (i in safeData.indices) {
                try {
                    val entry = safeData[i]
                    csvPrinter?.printRecord(entry.x, entry.y)
                } catch (e: IOException) {
                    result = false
                    break
                }
            }
        }
        csvPrinter?.close()
        return result
    }

}