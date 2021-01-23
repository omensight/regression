package com.alphemsoft.education.regression.dataexporter

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test


class ExportBehaviourBuilderTest {
    lateinit var context: Context

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
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun whenBuilding_buildACsvBehaviour() {
        val builder = ExportBehaviour.Builder(context, FileData.Csv("FILE_NAME"))
        Assert.assertTrue(builder.build() is CsvExportBehaviour)
    }

    @Test
    fun whenBuilding_buildAXlsBehaviour() {
        val builder = ExportBehaviour.Builder(context, FileData.Excel("PDF_FILE"))
        Assert.assertTrue(builder.build() is XlsxExportBehaviour)
    }

    @Test
    fun whenBuilding_buildReturnsNullIfNameIsEmpty() {
        val builder = ExportBehaviour.Builder(context, FileData.Excel(""))
        assertNull(builder.build())
    }

}