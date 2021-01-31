package com.alphemsoft.education.regression.data.regression

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LatexConverterTest {
    private val originalNumber = 123456.123456789
    private val latexConverter = LatexConverter()

    @Test
    fun whenRounding_containsJustOneWholeDigit(){
        val rounded = latexConverter.roundedNumber(originalNumber)
        val indexOfPoint = rounded.indexOf('.')
        assertThat(indexOfPoint).isEqualTo(1)
    }

    @Test
    fun whenRounding_containsDeterminedDecimalDigits(){
        val rounded = latexConverter.roundedNumber(originalNumber)
        val expected = "1.23456E5"
        assertThat(rounded).isEqualTo(expected)
    }

    @Test
    fun whenRounding_1point2Returns1point2(){
        val simpleNumber = 1.2
        val rounded = latexConverter.roundedNumber(simpleNumber)
        assertThat(rounded).isEqualTo("1.2")
    }

    @Test
    fun whenRounding_roundNumberSmallerThanOneToFiveDecimals(){
        val simpleNumber = 0.99999
        val rounded = latexConverter.roundedNumber(simpleNumber)
        assertThat(rounded).isEqualTo("0.99999")
    }

    @Test
    fun whenConverting_isExactlyTheSame(){
        val simpleNumber = 1.2
        val latex: String = latexConverter.toLatex(simpleNumber)
        assertThat(latex).isEqualTo("1.2")
    }

    @Test
    fun whenConverting_containsTimesWordIfNumberGraterThan10(){
        val simpleNumber = 10.0
        val latex = latexConverter.toLatex(simpleNumber)
        assertThat(latex).contains("\\times 10^1")
    }
}