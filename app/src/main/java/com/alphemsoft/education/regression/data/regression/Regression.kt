package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.data.model.secondary.Result

interface Regression {
    suspend fun getResults(decimals: Int): List<Result>
    suspend fun setData(data: List<DataPoint>)
    fun getCalculatedPoints(): List<Pair<Double, Double>>
}