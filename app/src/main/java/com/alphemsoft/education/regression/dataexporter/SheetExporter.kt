package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.SheetType
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.dataexporter.exportbehaviour.XlsxExportBehaviour
import com.alphemsoft.education.regression.extensions.getSimpleDate
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.OutputStream

class SheetExporter(
    private val context: Context
) {
    private val workBook: Workbook = XSSFWorkbook()
    companion object{
        const val EXTENSION = "xlsx"
    }

    private var uri: Uri? = null
    private var outputStream: OutputStream? = null
    private val outPutStreamGenerator: OutPutStreamGenerator = OutPutStreamGenerator()
    fun initialize(fileName: String): Uri? {
        val contentResolver: ContentResolver? = context.contentResolver
        this.uri = if (fileName.isNotEmpty()) {
            val uri = contentResolver?.let { outPutStreamGenerator.getUri(fileName, EXTENSION, it) }
            uri?.let {
                outputStream = contentResolver.openOutputStream(uri)
            }
            uri
        } else {
            null
        }
        return uri
    }

    fun export(sheet: Sheet): Boolean {
        require(uri != null) { "Not initialized" }
        outputStream?.let {
            val xlsxSheet = workBook.createSheet(context.getString(R.string.export_sheet_information))
            xlsxSheet.createRow(0).apply {
                createCell(0).setCellValue(context.getString(R.string.name))
                createCell(1).setCellValue(sheet.name)
            }
            xlsxSheet.createRow(1).apply {
                createCell(0).setCellValue(context.getString(R.string.created))
                createCell(1).setCellValue(sheet.createdOn.getSimpleDate())
            }
            xlsxSheet.createRow(2).apply {
                createCell(0).setCellValue(context.getString(R.string.regression_type))
                createCell(1).setCellValue(
                    when(sheet.type){
                        SheetType.LINEAR -> context.getString(R.string.sheet_type_linear)
                        SheetType.POWER -> context.getString(R.string.sheet_type_power)
                    }
                )
            }
            xlsxSheet.createRow(3).apply {
                createCell(0).setCellValue(context.getString(R.string.x_label))
                createCell(1).setCellValue(sheet.xLabel)
            }
            xlsxSheet.createRow(4).apply {
                createCell(0).setCellValue(context.getString(R.string.y_label))
                createCell(1).setCellValue(sheet.yLabel)
            }
        }
        return true
    }

    fun export(entries: List<SheetEntry>): Boolean {
        require(uri != null) { "Not initialized" }
        val xlsxExportBehaviour = XlsxExportBehaviour(workBook, outputStream!!, uri!!, false)
        return xlsxExportBehaviour.export(entries)
    }

    fun export(results: List<Result>) {
        require(uri != null) { "Not initialized" }
        val sheet = workBook.createSheet(context.getString(R.string.export_sheet_results))
        for (i in results.indices){
            val currentResult = results[i]
            val row = sheet.createRow(i)
            val descriptionCell = row.createCell(0)
            descriptionCell.setCellValue(context.getString(currentResult.title))
            val valueCell = row.createCell(1)
            currentResult.value?.let {
                valueCell.setCellValue(it)
            }
        }
    }

    fun save(): Boolean {
        return try {
            workBook.write(outputStream)
            true
        }catch (e: IOException){
            false
        }
    }

}
