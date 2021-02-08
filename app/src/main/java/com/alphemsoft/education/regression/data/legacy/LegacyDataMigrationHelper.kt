package com.alphemsoft.education.regression.data.legacy

import android.content.Context
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.SheetType
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class LegacyDataMigrationHelper constructor(
    private val context: Context
){
    val sheetList: MutableList<Sheet> = ArrayList()
    val sheetEntries: MutableList<SheetEntry> = ArrayList()
    private lateinit var oldDBHelper: OldDBHelper

    fun readData(minSheetId: Long, minEntryId: Long){
        sheetList.clear()
        sheetEntries.clear()
        oldDBHelper= OldDBHelper(context)
        val saved = oldDBHelper.savedRegressions
        var globalEntryId = minEntryId+1
        var globalSheetId = minSheetId+1
        saved?.forEach {regressionLegacy->
            val type = when(regressionLegacy.type){
                0 -> SheetType.LINEAR
                else -> SheetType.POWER
            }
            val sheet = Sheet(
                globalSheetId++,
                type,
                regressionLegacy.name,
                regressionLegacy.xLabel,
                regressionLegacy.yLabel,
                Date(),
                2
            )
            sheetList.add(sheet)
            val legacyDataPairs = regressionLegacy.getDataPairs().mapIndexed { index, dataPair ->
                SheetEntry(globalEntryId++, sheet.id, BigDecimal(dataPair.x), BigDecimal(dataPair.y))
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