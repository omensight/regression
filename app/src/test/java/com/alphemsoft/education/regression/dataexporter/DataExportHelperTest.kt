package com.alphemsoft.education.regression.dataexporter

import android.content.ContentValues
import android.content.Context
import com.alphemsoft.education.regression.dataexporter.testfactory.SheetEntryFactory
import com.alphemsoft.education.regression.helper.mock
import com.alphemsoft.education.regression.helper.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.powermock.modules.junit4.PowerMockRunner
import java.lang.IllegalArgumentException

@RunWith(PowerMockRunner::class)
class DataExportHelperTest {
    lateinit var dataExportHelper: DataExportHelper
    val sheeEntrytList = SheetEntryFactory.getRandomList(5)
    @Mock
    private lateinit var exportBehaviour: ExportBehaviour

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var contentValues: ContentValues

    private val sheetItemList = SheetEntryFactory.getRandomList(5)

    @Before
    fun setUp(){
        dataExportHelper = spy(DataExportHelper())
        whenever(context.contentResolver).thenReturn(mock())
    }

    @Test(expected = IllegalArgumentException::class)
    fun export_throwExceptionIfExportBehaviourNotSet(){
        dataExportHelper.export(sheeEntrytList)
    }

    @Test()
    fun export_callExportIfBehaviourIsSet(){
        dataExportHelper.exportBehaviour = exportBehaviour
        dataExportHelper.export(sheeEntrytList)
        verify(exportBehaviour).export(sheeEntrytList)
    }

    @Test
    fun export_returnsTrueWhenDataIsExported(){
        dataExportHelper.exportBehaviour = exportBehaviour

        `when`(exportBehaviour.export(anyList())).thenReturn(true)
        val exported = dataExportHelper.export(sheeEntrytList)
        Assert.assertTrue(exported)
    }


}