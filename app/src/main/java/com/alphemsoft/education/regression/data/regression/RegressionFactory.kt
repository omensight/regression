package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.data.model.SheetType

object RegressionFactory {
    fun getRegression(regressionType: SheetType): Regression{
        return when(regressionType){
            SheetType.LINEAR -> LinearRegression()
            SheetType.POWER -> PowerRegression()
            SheetType.MULTIPLE_LINEAR -> TODO("Not implemented")
        }
    }
}