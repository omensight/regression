package com.alphemsoft.education.regression

import com.alphemsoft.education.regression.premium.feature.ExportDataFeature
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class ExportDocumentFeatureUnitTest {

    private lateinit var exportDataFeature: ExportDataFeature

    @Before
    fun setup(){
        exportDataFeature = ExportDataFeature("Register2020")
    }

    @Test
    fun afterExecute_returnsTrue(){
        runBlocking {
            Assert.assertTrue(exportDataFeature.execute())
        }
    }

    @Test
    fun documentName_notNull(){
        exportDataFeature = ExportDataFeature("Register2020")
        Assert.assertNotNull(exportDataFeature.registerName)
    }
}