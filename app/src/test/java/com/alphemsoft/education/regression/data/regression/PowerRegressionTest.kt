package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.data.model.SheetEntry
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class PowerRegressionTest {

    private lateinit var regression: PowerRegression
    private lateinit var data: List<SheetEntry>

    @Before
    fun setup(){
        regression = PowerRegression()
        data = listOf(
            SheetEntry(0, 0, BigDecimal("369"), BigDecimal("5")),
            SheetEntry(1, 0, BigDecimal("95"), BigDecimal("63")),
            SheetEntry(2, 0, BigDecimal("63"), BigDecimal("69")),
        )
    }

    @Test
    fun whenGettingOriginalData_itHasTheSameSizeThanTheInput(){
        runBlocking {
            regression.setData(data)
            assertThat(regression.getOriginalDataLine().dataPoints).hasSize(data.size)
        }
    }

    @Test
    fun whenGettingCalculatedLines_isNotEmpty(){
        runBlocking {
            regression.setData(data)
            assertThat(regression.getCalculatedLines()).hasSize(1)
        }
    }

    @Test
    fun whenGettingFixedData_isNotEmpty(){
        runBlocking {
            regression.setData(data)
            assertThat(regression.getCalculatedLines()[0].dataPoints).isNotEmpty()
        }
    }
}