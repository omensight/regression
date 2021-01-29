package com.alphemsoft.education.regression.dataexporter.exportbehaviour

import com.alphemsoft.education.regression.dataexporter.testfactory.SheetEntryFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

class ExportBehaviourTest{
    lateinit var fileName: String
    lateinit var exportBehaviour: ExportBehaviour
    private val sheetEntries = SheetEntryFactory.getRandomList(5)

    @Before
    fun setup(){
        fileName = "FILE_NAME"
        exportBehaviour = mock()
        whenever(exportBehaviour.export(anyList())).thenReturn(true)
    }

    @Test
    fun whenExporting_returnsTrue(){
        Assert.assertTrue(exportBehaviour.export(sheetEntries))
    }

}