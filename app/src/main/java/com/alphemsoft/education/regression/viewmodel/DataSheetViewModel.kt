package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class DataSheetViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val dataPointDataSource: IDataPointLocalDataSource,
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val dataEntries: MutableStateFlow<MutableList<SheetEntry>> = MutableStateFlow(ArrayList())

    val _importEntries: MutableLiveData<List<SheetEntry?>> = MutableLiveData()
    val importEntries: MutableLiveData<List<SheetEntry?>>
        get() = _importEntries

    fun getAllSheets(): Flow<PagingData<Sheet>> {
        return sheetDataSource.findAll().flow.cachedIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    suspend fun getDataPointList(sheetId: Long): Flow<List<SheetEntry>> {
        dataEntries.value = ArrayList(dataPointDataSource.getDataPointList(sheetId))
        return dataEntries
    }

    @ExperimentalCoroutinesApi
    suspend fun addTemporaryPoint(sheetId: Long) {
        val value = dataEntries.value + SheetEntry(id = 0, fkSheetId = sheetId, null, null, false)
        dataEntries.value = ArrayList(value)
    }

    @ExperimentalCoroutinesApi
    suspend fun sweepAll() {
        val value = ArrayList<SheetEntry>(dataEntries.value)
        value.forEach {
            it.data = null
        }
        dataEntries.value = ArrayList(value.filter { true })
    }

    @ExperimentalCoroutinesApi
    suspend fun deleteSelected() {
        val value = ArrayList(dataEntries.value)
        value.removeAll { it.selected }
        dataEntries.value = value
    }

    @ExperimentalCoroutinesApi
    suspend fun selectNothing() {
        val value = dataEntries.value
        value.filter { it.selected }.forEach {
            it.selected = false
        }
        dataEntries.value = ArrayList(dataEntries.value)
    }

    @ExperimentalCoroutinesApi
    suspend fun selectAll() {
        val value = ArrayList(dataEntries.value)
        value.forEach {
            it.selected = true
        }
        dataEntries.value = value
    }

    suspend fun addTemporaryPoints(numberOfEmptyDataPoints: Int, sheetId: Long) {
        val value = ArrayList(dataEntries.value)
        for (i in 0 until numberOfEmptyDataPoints) {
            value.add(SheetEntry(0, sheetId, null, null, false))
        }
        dataEntries.value = value
    }

    suspend fun validateAndSaveData(): Boolean {
        val value = dataEntries.value
        val firstNullData = value.firstOrNull { dataPoint ->
            dataPoint.data == null
        }
        return firstNullData?.let {
            false
        } ?: run {
            val existingEntities = value.filter {
                it.id != 0L
            }
            val newEntities = value.filter {
                it.id == 0L
            }
            dataPointDataSource.insert(newEntities)
            dataPointDataSource.update(existingEntities)
            true
        }
    }

    suspend fun addTemporaryEntries(entries: List<Array<Double?>>, sheetId: Long){
        val oldData = dataEntries.value
        val last = oldData.lastOrNull()
        val sheet = sheetDataSource.find(sheetId)
        val startX = last?.xCoordinate?.plus(1) ?: 0

        val newDataEntries = entries.map {
            SheetEntry(0,sheetId, )
        }
        val newList = ArrayList(dataEntries.value)
        newList.addAll(newDataEntries)
        dataEntries.value = newList
    }

    fun addImportEntries(entryList: List<Array<Double?>>) {
        val max = entryList.map { it.size }.maxOrNull()
        val newEntries = ArrayList<SheetEntry?>()
        max?.let {
            for (currentEntryList in entryList){
                for (i in 0 until max){
                    try {
                        val currentDouble = currentEntryList[i]
                        val entry = SheetEntry(-1,-1,-1,-1, BigDecimal(currentDouble?:0.0))
                        newEntries.add(entry)
                    }catch (t: Throwable){
                        newEntries.add(null)
                    }
                }
            }
        }
        _importEntries.postValue(newEntries)
    }
}