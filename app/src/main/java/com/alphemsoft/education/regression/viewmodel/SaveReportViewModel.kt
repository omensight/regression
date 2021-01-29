package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetEntryLocalDataSource
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.SheetEntry

class SaveReportViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val sheetEntryLocalDataSource: ISheetEntryLocalDataSource
): ViewModel() {
    val fileNameLiveData = MutableLiveData<String>()

    suspend fun initialize(sheetId: Long, now: String) {
        sheetDataSource.find(sheetId)?.let {sheet ->
            if (fileNameLiveData.value.isNullOrEmpty()){
                fileNameLiveData.postValue("${sheet.name}-$now")
            }
        }
    }

    suspend fun getSheet(sheetId: Long): Sheet? {
        return sheetDataSource.find(sheetId)
    }

    suspend fun getSheetEntries(sheetId: Long): List<SheetEntry> {
        return sheetEntryLocalDataSource.getDataPointList(sheetId)
    }
}