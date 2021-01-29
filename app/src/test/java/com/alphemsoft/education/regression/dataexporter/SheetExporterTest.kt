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
import com.alphemsoft.education.regression.dataexporter.testfactory.SheetEntryFactory
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

@RunWith(PowerMockRunner::class)
@PrepareForTest(SheetExporter::class)
class SheetExporterTest {

    private lateinit var sheetExporter: SheetExporter
    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver
    private lateinit var outPutStreamGenerator: OutPutStreamGenerator
    private lateinit var uri: Uri
    val FILE_NAME = "SOME_FILE_NAME"
    val FILE_EXTENSION_CSV = "csv"
    private val sheetEntries: List<SheetEntry> = SheetEntryFactory.getRandomList(5)
    private val sheets = ArrayList<XSSFSheet>()
    private val rows = ArrayList<XSSFRow>()
    private val cells = ArrayList<XSSFCell>()
    private lateinit var sheet: Sheet
    lateinit var workbook: XSSFWorkbook
    private lateinit var xlsxExportBehaviour: XlsxExportBehaviour
    private lateinit var outputStream: OutputStream
    private val results: List<Result> = listOf(
        Result(R.string.buy_subscription_enjoy_title,"FORM", 256.36),
        Result(R.string.buy_subscription_enjoy_title,"FORM", 256.36),
        Result(R.string.buy_subscription_enjoy_title,"FORM", 256.36)
    )
    @Before
    fun setup(){
        workbook = mock()
        xlsxExportBehaviour = mock()
        PowerMockito.whenNew(XSSFWorkbook::class.java).withNoArguments().thenReturn(workbook)
        PowerMockito.whenNew(XlsxExportBehaviour::class.java).withAnyArguments().thenReturn(xlsxExportBehaviour)

        context = mock()
        whenever(context.getString(anyInt())).thenReturn("ANY_STRING")
        contentResolver = mock()
        outPutStreamGenerator = mock()
        PowerMockito.whenNew(OutPutStreamGenerator::class.java).withNoArguments().thenReturn(outPutStreamGenerator)
        outputStream = mock()
        uri = mock()
        sheet = Sheet(0,SheetType.LINEAR, "SheetName", "X", "Y", Date(), 2)
        whenever(outPutStreamGenerator.getUri(anyString(), anyString(), any())).thenReturn(uri)
        whenever(context.contentResolver).thenReturn(contentResolver)
        whenever(contentResolver.openOutputStream(any())).thenReturn(outputStream)
        sheetExporter = SheetExporter(context)

    }

    private fun createSheet(): XSSFSheet {
        val xssfSheet: XSSFSheet = mock()
        sheets.add(xssfSheet)
        return xssfSheet
    }

    @Test
    fun whenInitializing_contextUseContentResolver(){
        setupOutputStream()
        sheetExporter.initialize("SOME_NAME")
        Mockito.verify(context).contentResolver
    }

    @Test
    fun whenInitializing_getsUriFromOutputStreamGenerator(){
        val uri: Uri = mock()
        whenever(outPutStreamGenerator.getUri(anyString(), anyString(), any())).thenReturn(uri)
        assertThat(sheetExporter.initialize("SOME_NAME")).isEqualTo(uri)
    }

    @Test
    fun whenInitializing_getUri(){
        val uri: Uri = mock()
        whenever(outPutStreamGenerator.getUri(anyString(), anyString(), any())).thenReturn(uri)
        sheetExporter.initialize("SOME_NAME")
        verify(outPutStreamGenerator).getUri(anyString(), anyString(), any())
    }

    @Test
    fun whenInitializing_uriNotNullIfNameAndExtensionSet(){
        val uri: Uri = mock()
        whenever(outPutStreamGenerator.getUri(anyString(), anyString(), any())).thenReturn(uri)
        assertThat(sheetExporter.initialize("SOME_NAME")).isNotNull()
    }

    @Test
    fun whenInitializing_uriNullIfNameAndExtensionNotSet(){
        val uri: Uri = mock()
        whenever(outPutStreamGenerator.getUri(anyString(), anyString(), any())).thenReturn(uri)
        assertThat(sheetExporter.initialize("")).isNull()
    }

    @Test
    fun whenExporting_openOutputStream(){
        setupOutputStream()

        sheetExporter.initialize(FILE_NAME)
        sheetExporter.export(sheetEntries)
        verify(contentResolver).openOutputStream(any())
    }

    @Test(expected = IllegalArgumentException::class)
    fun whenExporting_ifNotInitializedThrowsIllegalException(){
        sheetExporter.export(sheetEntries)
    }

    @Test
    fun afterExporting_returnTrue(){
        whenever(workbook.createSheet()).thenReturn(mock())
        whenever(workbook.createSheet(anyString())).thenReturn(mock())
        whenever(xlsxExportBehaviour.export(ArgumentMatchers.anyList())).thenReturn(true)
        sheetExporter.initialize(FILE_NAME)
        assertThat(sheetExporter.export(sheetEntries)).isTrue()
    }

    @Test
    fun whenExporting_useSheetDataToCreateSheetPage(){
        setupSheetInvocations()
        val sheetSpy = spy(sheet)
        setupOutputStream()
        sheetExporter.initialize(FILE_NAME)
        sheetExporter.export(sheetSpy)
        verify(sheetSpy).name
        verify(sheetSpy).createdOn
        verify(sheetSpy).type
        verify(sheetSpy).xLabel
        verify(sheetSpy).yLabel
    }

    private fun setupSheetInvocations() {
        whenever(workbook.createSheet(anyString())).doAnswer{
            val sheet = getMockedSheet()
            sheets.add(sheet)
            whenever(sheet.createRow(anyInt())).doAnswer {
                val row: XSSFRow = mock()
                rows.add(row)
                whenever(row.createCell(anyInt())).doAnswer {
                    val cell: XSSFCell = mock()
                    cells.add(cell)
                    return@doAnswer cell
                }
                return@doAnswer row
            }
            return@doAnswer sheet
        }
    }

    @Test
    fun whenExporting_createSheet(){

        setupSheetInvocations()
        setupOutputStream()
        sheetExporter.initialize(FILE_NAME)
        sheetExporter.export(sheet)
        verify(workbook).createSheet(anyString())
    }

    private fun getMockedSheet(): XSSFSheet {
        val sheet: XSSFSheet = mock()
        return sheet
    }

    @Test
    fun whenExporting_exportData(){
        setupOutputStream()
        val xlsxExportBehaviour: XlsxExportBehaviour = mock()
        PowerMockito.whenNew(XlsxExportBehaviour::class.java).withAnyArguments().thenReturn(xlsxExportBehaviour)
        sheetExporter.initialize(FILE_NAME)
        sheetExporter.export(sheetEntries)
        verify(xlsxExportBehaviour).export(any())
    }

    @Test
    fun whenExporting_getEachResult(){
        setupSheetInvocations()
        setupOutputStream()
        initializeSheetExporter()
        val spyResults = spy(results)
        sheetExporter.export(spyResults)
        verify(spyResults, times(results.size))[any()]
    }

    private fun setupOutputStream() {
        val outputStream: OutputStream = mock()
        whenever(contentResolver.openOutputStream(any())).thenReturn(outputStream)
    }

    @Test
    fun whenExportingResults_verifySheetCreation(){
        setupSheetInvocations()
        setupOutputStream()
        initializeSheetExporter()
        sheetExporter.export(results)
        verify(workbook).createSheet(anyString())
    }

    @Test
    fun whenExportingResults_verifyRowsCreation(){
        setupSheetInvocations()
        setupOutputStream()
        initializeSheetExporter()
        sheetExporter.export(results)
        assertThat(rows).isNotEmpty()
    }

    @Test
    fun whenExportingResults_verifyCellsCreation(){
        setupSheetInvocations()
        initializeSheetExporter()
        sheetExporter.export(results)
        rows.forEach{
            verify(it, times(3)).createCell(anyInt())
        }
    }

    @Test
    fun whenSaving_workbookCallsWrite(){
        setupSheetInvocations()
        sheetExporter.initialize(FILE_NAME)
        sheetExporter.save()
        verify(workbook).write(any())
    }

    private fun initializeSheetExporter() {
        sheetExporter.initialize(FILE_NAME)
    }

    @Test
    fun whenSaving_returnFalseIfGetAnyException(){
        whenever(workbook.write(any())).thenThrow(IOException::class.java)
        setupSheetInvocations()
        initializeSheetExporter()
        assertThat(sheetExporter.save()).isFalse()
    }

    @Test
    fun whenSaving_returnTrueIfGetAnyException(){
        setupSheetInvocations()
        initializeSheetExporter()
        assertThat(sheetExporter.save()).isTrue()
    }

    @After
    fun emptyAll(){
        sheets.clear()
        rows.clear()
        cells.clear()
    }

}