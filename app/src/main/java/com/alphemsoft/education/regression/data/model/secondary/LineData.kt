package com.alphemsoft.education.regression.data.model.secondary

import androidx.annotation.StringRes

data class LineData constructor(
    val lineNameString: String?,
    @StringRes val lineNameResource: Int?,
    val dataPoints: List<Pair<Double, Double>>
){
    constructor(lineNameString: String, dataPoints: List<Pair<Double, Double>>): this(lineNameString,null, dataPoints)
    constructor(@StringRes lineNameResource: Int, dataPoints: List<Pair<Double, Double>>): this(null, lineNameResource, dataPoints)
}