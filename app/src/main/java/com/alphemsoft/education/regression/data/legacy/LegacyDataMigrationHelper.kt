package com.alphemsoft.education.regression.data.legacy

import android.content.Context
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.SheetType
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class LegacyDataMigrationHelper constructor(private val context: Context){
    val sheetList: MutableList<Sheet> = ArrayList()
    val sheetEntries: MutableList<SheetEntry> = ArrayList()
    private lateinit var oldDBHelper: OldDBHelper

    fun readData(){
        sheetList.clear()
        sheetEntries.clear()
        oldDBHelper= OldDBHelper(context)
        val saved = oldDBHelper.savedRegressions
        saved?.forEach {regressionLegacy->
            val type = when(regressionLegacy.type){
                0 -> SheetType.LINEAR
                else -> SheetType.POWER
            }
            val sheet = Sheet(
                regressionLegacy.id.toLong(),
                type,
                regressionLegacy.name,
                regressionLegacy.xLabel,
                regressionLegacy.yLabel,
                Date(),
                2
            )
            sheetList.add(sheet)
            val legacyDataPairs = regressionLegacy.dataPairs.map { dataPairLegacy->
                SheetEntry(0, sheet.id, BigDecimal(dataPairLegacy.x), BigDecimal(dataPairLegacy.y))
            }
            sheetEntries.addAll(legacyDataPairs)
        }
    }

    fun deleteSheets(){
        oldDBHelper.regressionNames?.forEach {
            oldDBHelper.deleteRegression(it)
        }

    }

}