package com.alphemsoft.education.regression.dataexporter.exportbehaviour

import android.net.Uri
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.dataexporter.testfactory.SheetEntryFactory
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.io.OutputStream
import java.math.BigDecimal

class XlsxExportBehaviourTest {
    private lateinit var xlsxExportBehaviour: XlsxExportBehaviour
    private lateinit var outputStream: OutputStream

    private lateinit var workbook: XSSFWorkbook
    private lateinit var sheet: XSSFSheet
    private lateinit var sheetEntries: List<SheetEntry>
    private lateinit var rows: MutableList<Row>
    private lateinit var cells: MutableList<Cell>
    private lateinit var uri: Uri

    val corruptedData = listOf(SheetEntry(0, 0, null, BigDecimal("1.2")))

    @Before
    fun setup() {
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
        sheetEntries = SheetEntryFactory.getRandomList(5)
        outputStream = mock()
        workbook = mock()
        sheet = mock()
        whenever(workbook.createSheet(anyString())).thenReturn(sheet)
        uri = mock()
        xlsxExportBehaviour = XlsxExportBehaviour(workbook, outputStream, uri)
        rows = ArrayList()
        cells = ArrayList()
    }

    @Test
    fun whenInstantiating_createSheet() {
        verify(workbook, times(1)).createSheet(anyString())
    }

    @Test
    fun whenExporting_returnsTrue() {
        setupAnswerOfMockedRows()
        Assert.assertTrue(xlsxExportBehaviour.export(sheetEntries))
    }

    @Test
    fun whenExporting_createRow() {
        setupAnswerOfMockedRows()
        xlsxExportBehaviour.export(sheetEntries)
        verify(sheet, atLeast(1)).createRow(anyInt())
    }

    @Test
    fun whenExporting_createRow_calledSheetEntriesSizeTimes() {
        setupAnswerOfMockedRows()
        xlsxExportBehaviour.export(sheetEntries)
        verify(sheet, times(sheetEntries.size)).createRow(anyInt())
    }


    @Test
    fun whenExporting_verifySizeOfCellsAreTwiceTheRows() {
        setupAnswerOfMockedRows()
        Assert.assertEquals(rows.size * 2, cells.size)
    }

    private fun setupAnswerOfMockedRows() {
        doAnswer {
            val row: XSSFRow = mock()
            rows.add(row)
            whenever(row.createCell(anyInt())).thenReturn(getMockedCell())
            return@doAnswer row
        }.`when`(sheet).createRow(anyInt())
    }

    private fun getMockedCell(): XSSFCell {
        val cell: XSSFCell = mock()
        cells.add(cell)
        return cell
    }

    @Test
    fun whenExporting_createTwiceCells() {
        setupAnswerOfMockedRows()
        xlsxExportBehaviour.export(sheetEntries)
        rows.forEach {
            verify(it, times(2)).createCell(anyInt())
        }
    }

    @Test
    fun whenExporting_returnFalseIfDataIsCorrupted() {
        Assert.assertFalse(xlsxExportBehaviour.export(corruptedData))
    }

    @Test
    fun whenInstantiating_callToWriteExportNotCalled() {
        verify(workbook, never()).write(any())
    }

    @Test
    fun whenExporting_workbookCallToWrite_withValidData() {
        setupAnswerOfMockedRows()
        xlsxExportBehaviour.export(sheetEntries)
        verify(workbook, times(1)).write(any())
    }

    @Test
    fun whenExporting_workbookNotCallToWrite_withInvalidData() {
        setupAnswerOfMockedRows()
        xlsxExportBehaviour.export(corruptedData)
        verify(workbook, never()).write(any())
    }

    @Test
    fun whenExporting_notCallToSaveIfTheFileNotEnds(){
        xlsxExportBehaviour = XlsxExportBehaviour(workbook, outputStream, uri, false)
        setupAnswerOfMockedRows()
        xlsxExportBehaviour.export(sheetEntries)
        verify(workbook, never()).write(any())
    }

}