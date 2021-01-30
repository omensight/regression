package com.alphemsoft.education.regression.dataexporter.exportbehaviour

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.dataexporter.testfactory.SheetEntryFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.powermock.modules.junit4.PowerMockRunner
import java.io.OutputStream

@RunWith(PowerMockRunner::class)
class CsvExportBehaviourUnitTest {

    lateinit var csvExportBehaviour: CsvExportBehaviour

    lateinit var contentValues: ContentValues
    lateinit var contentResolver: ContentResolver

    private lateinit var sheetList: List<SheetEntry>
    private lateinit var outputStream: OutputStream

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        contentValues = mock()
        sheetList = SheetEntryFactory.getRandomList(5)
        val context: Context = mock()
        contentResolver = mock()
        whenever(context.contentResolver).thenReturn(contentResolver)
        whenever(contentResolver.insert(any(), any())).thenReturn(mock())
        outputStream = mock()
        val uri: Uri = mock()
        csvExportBehaviour = CsvExportBehaviour(outputStream, uri)
        whenever(contentResolver.openOutputStream(any())).thenReturn(mock())
    }

    @Test
    fun whenExporting_outputStreamWrites(){
        csvExportBehaviour.export(sheetList)
        verify(outputStream).write(any(), anyInt(), anyInt())
    }




}