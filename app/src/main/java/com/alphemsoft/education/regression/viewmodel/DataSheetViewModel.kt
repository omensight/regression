package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.SheetEntry
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class DataSheetViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val dataPointDataSource: IDataPointLocalDataSource,
) : ViewModel() {

    val dataEntries: MutableLiveData<MutableList<SheetEntry>> = MutableLiveData(ArrayList())

    private val _importedEntries: MutableLiveData<List<SheetEntry>> = MutableLiveData()
    val importedEntries: LiveData<List<SheetEntry>>
        get() = _importedEntries

    suspend fun getSheet(sheetId: Long): Sheet? {
        return sheetDataSource.find(sheetId)
    }

    fun getAllSheets(): Flow<PagingData<Sheet>> {
        return sheetDataSource.findAll().flow.cachedIn(viewModelScope)
    }

   
    suspend fun getDataPointList(sheetId: Long): Flow<List<SheetEntry>> {
        dataEntries.postValue(ArrayList(dataPointDataSource.getDataPointList(sheetId)))
        return dataEntries.asFlow()
    }

   
    suspend fun addTemporaryPoint(sheetId: Long) {
        val value = dataEntries.value
        value?.add(SheetEntry(
            0L,
            sheetId,
            null,
            null,
            false,
            Date().time
        ))
        dataEntries.postValue(value)
    }

   
    suspend fun sweepAll() {
        val value = ArrayList<SheetEntry>(dataEntries.value)
        value.forEach {
            it.x = null
            it.y = null
        }
        dataEntries.postValue(value)
    }

   
    suspend fun deleteSelected() {
        val value = ArrayList(dataEntries.value)
        value.filter { it.selected
        }.forEach {
            it.deleted = true
        }
        dataEntries.postValue(value)
    }

   
    suspend fun selectNothing() {
        val value = dataEntries.value
        value?.filter { it.selected }?.forEach {
            it.selected = false
        }
        dataEntries.postValue(dataEntries.value)
    }

   
    suspend fun selectAll() {
        val value = ArrayList(dataEntries.value)
        value.forEach {
            it.selected = true
        }
        dataEntries.postValue(value)
    }

    suspend fun addTemporaryPoints(numberOfEmptyDataPoints: Int, sheetId: Long) {
        val value = ArrayList(dataEntries.value)
        for (i in 0 until numberOfEmptyDataPoints) {
            value.add(SheetEntry(0, sheetId, null, null, false, Random.nextLong()))
        }
        dataEntries.postValue(value)
    }

    suspend fun validateAndSaveData(): Boolean {
        val value = dataEntries.value
        val firstNullData = value?.firstOrNull { dataPoint ->
            dataPoint.x == null || dataPoint.y == null
        }
        return firstNullData?.let {
            false
        } ?: run {
            value?.filter {
                it.id != 0L
            }?.let {
                dataPointDataSource.update(it)
            }
            value?.filter {
                it.id == 0L
            }?.let {
                dataPointDataSource.insert(it)
            }
            value?.filter {
                it.deleted && it.id != 0L
            }?.run {
                dataPointDataSource.delete(this)
            }
            true
        }
    }

    fun addImportedEntries(entryList: List<Pair<Double, Double>>, sheetId: Long) {
        val temp = entryList.map {
            SheetEntry(0,sheetId, BigDecimal(it.first), BigDecimal(it.second), tempHash = Date().time)
        }
        _importedEntries.value = temp
    }

    fun emptyImportedData() {
        _importedEntries.value = null
    }

    fun addImportedDataToDataList() {
        val importedItems = _importedEntries.value?:ArrayList()
        val oldDataEntries = dataEntries.value?:ArrayList()
        oldDataEntries.addAll(importedItems)
        dataEntries.value = oldDataEntries
    }
}