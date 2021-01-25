package com.alphemsoft.education.regression.dataexporter

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.dataexporter.testfactory.SheetEntryFactory
import com.alphemsoft.education.regression.helper.mock
import com.alphemsoft.education.regression.helper.whenever
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.OutputStream

@RunWith(PowerMockRunner::class)
@PrepareForTest(MediaStore::class)
class CsvExportBehaviourUnitTest {

    lateinit var csvExportBehaviour: CsvExportBehaviour

    lateinit var contentValues: ContentValues
    lateinit var contentResolver: ContentResolver

    private lateinit var sheetList: List<SheetEntry>
    private lateinit var outputStream: OutputStream

    @Before
    fun setup(){
        PowerMockito.mockStatic(MediaStore.Files::class.java)
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