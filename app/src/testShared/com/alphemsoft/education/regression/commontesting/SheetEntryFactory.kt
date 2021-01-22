package com.alphemsoft.education.regression.dataexporter.testfactory

import com.alphemsoft.education.regression.data.model.SheetEntry
import java.math.BigDecimal
import kotlin.random.Random

object SheetEntryFactory {
    private fun createRandomSheetEntry(): SheetEntry {
        return SheetEntry(
            id = Random.nextLong(),
            fkSheetId = 0,
            x = BigDecimal(Random.nextDouble(from = 0.0, until = 2000.0)),
            y = BigDecimal(Random.nextDouble(from = 0.0, until = 2000.0))
        )
    }

    fun getRandomList(size: Int): List<SheetEntry>{
        val list = mutableListOf<SheetEntry>()
        for (i in 0 until size){
            list += createRandomSheetEntry()
        }
        return list
    }
}