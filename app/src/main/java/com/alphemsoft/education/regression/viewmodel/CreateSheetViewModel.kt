package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.ISheetColumnLocalDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.datasource.base.DataPointLocalDataSource
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.SheetColumn
import java.util.*

class CreateSheetViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val sheetColumnLocalDataSource: ISheetColumnLocalDataSource
) : ViewModel() {

    val newSheet = MutableLiveData<Sheet>()

    init {
        newSheet.value = Sheet(
            id = 0,
            name = "",
            createdOn = Date(-1)
        )
    }

    suspend fun insertNewSheet(): Long {
        val sheet = newSheet.value?.copy(createdOn = Date())
        require(sheet != null)
        val sheetId =  sheetDataSource.insert(sheet)
        sheetColumnLocalDataSource.insert(SheetColumn(0,sheetId,0))
        sheetColumnLocalDataSource.insert(SheetColumn(0,sheetId,1))
        return sheetId
    }
}