package com.alphemsoft.education.regression.dataexporter.exportbehaviour

import android.net.Uri
import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter

class CsvExportBehaviour(
    private val outputStream: OutputStream,
    uri: Uri
) : ExportBehaviour(uri) {

    private var csvPrinter: CSVPrinter? = null

    override fun export(data: List<SheetEntry>): Boolean {
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