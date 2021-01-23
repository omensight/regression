package com.alphemsoft.education.regression.dataexporter

import android.content.Context
import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.poi.xssf.usermodel.XSSFWorkbook

interface ExportBehaviour {

    fun export(data: List<SheetEntry>): Boolean

    class Builder(
        private val context: Context,
        private val fileData: FileData
    ) {
        fun build(): ExportBehaviour? {
            val contentResolver = context.contentResolver
            val outputStream = OutPutStreamGenerator.generate(fileData, contentResolver)
            return outputStream?.let {
                when (fileData) {
                    is FileData.Csv -> {
                        CsvExportBehaviour(outputStream)
                    }
                    is FileData.Excel -> {
                        System.setProperty(
                            "org.apache.poi.javax.xml.stream.XMLInputFactory",
                            "com.fasterxml.aalto.stax.InputFactoryImpl"
                        )
                        System.setProperty(
                            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                            "com.fasterxml.aalto.stax.OutputFactoryImpl"
                        )
                        System.setProperty(
                            "org.apache.poi.javax.xml.stream.XMLEventFactory",
                            "com.fasterxml.aalto.stax.EventFactoryImpl"
                        )
                        XlsxExportBehaviour(XSSFWorkbook(), outputStream)
                    }
                }
            }
        }
    }

}

