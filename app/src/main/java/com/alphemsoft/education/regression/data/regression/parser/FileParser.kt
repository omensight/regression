package com.alphemsoft.education.regression.data.regression.parser

import androidx.annotation.Dimension
import java.io.InputStream

interface FileParser {
    fun parse(inputStream: InputStream, maxColumnCount: Int)
    fun getDataPoints()
}