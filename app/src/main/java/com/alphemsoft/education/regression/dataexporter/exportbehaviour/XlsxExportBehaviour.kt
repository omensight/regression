package com.alphemsoft.education.regression.dataexporter.exportbehaviour

import android.net.Uri
import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.OutputStream

class XlsxExportBehaviour(
    private val workbook: Workbook,
    private val outputStream: OutputStream,
    uri: Uri,
    private val hasToWriteTheFile: Boolean = true
) : ExportBehaviour(uri) {

    private val sheet: Sheet = workbook.createSheet("Data")

    override fun export(data: List<SheetEntry>): Boolean {
        var validData = true
        for (i in data.indices){
            val sheetEntry = data[i]
            val currentRow = sheet.createRow(i)
            val x = sheetEntry.x
            val y = sheetEntry.y
            if (x == null || y == null){
                validData = false
                break
            }else{
                val xCell = currentRow.createCell(0)
                val yCell = currentRow.createCell(1)
                xCell.setCellValue(sheetEntry.x?.toDouble()!!)
                yCell.setCellValue(sheetEntry.y?.toDouble()!!)
            }
        }
        if (validData && hasToWriteTheFile){
            workbook.write(outputStream)
        }
        return validData
    }

}
