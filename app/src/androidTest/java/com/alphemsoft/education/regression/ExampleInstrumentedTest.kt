package com.alphemsoft.education.regression

import android.content.ContentValues
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.apache.commons.math3.stat.regression.MultipleLinearRegression
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(PowerMockRunner::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.alphemsoft.education.regression", appContext.packageName)
    }

    @Test
    fun anyTest(){
        val contentValues: ContentValues = ContentValues()
        val anyInterface = mock(AnyInterface::class.java)
        anyInterface.foo()
        verify(anyInterface).foo()
    }
}

interface AnyInterface{
    fun foo()
    suspend fun bar()
}