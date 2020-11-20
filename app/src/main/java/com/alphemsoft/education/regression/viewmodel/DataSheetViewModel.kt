package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

@ExperimentalCoroutinesApi
class DataSheetViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val dataPointDataSource: IDataPointLocalDataSource,
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val dataEntries: MutableLiveData<MutableList<SheetEntry>> = MutableLiveData(ArrayList())

    private val _importEntries: MutableLiveData<List<SheetEntry?>> = MutableLiveData()
    val importEntries: MutableLiveData<List<SheetEntry?>>
        get() = _importEntries

    suspend fun getSheet(sheetId: Long): Sheet? {
        return sheetDataSource.find(sheetId)
    }

    fun getAllSheets(): Flow<PagingData<Sheet>> {
        return sheetDataSource.findAll().flow.cachedIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    suspend fun getDataPointList(sheetId: Long): Flow<List<SheetEntry>> {
        dataEntries.postValue(ArrayList(dataPointDataSource.getDataPointList(sheetId)))
        return dataEntries.asFlow()
    }

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    suspend fun sweepAll() {
        val value = ArrayList<SheetEntry>(dataEntries.value)
        value.forEach {
            it.x = null
            it.y = null
        }
        dataEntries.postValue(value)
    }

    @ExperimentalCoroutinesApi
    suspend fun deleteSelected() {
        val value = ArrayList(dataEntries.value)
        value.filter { it.selected
        }.forEach {
            it.deleted = true
        }
        dataEntries.postValue(value)
    }

    @ExperimentalCoroutinesApi
    suspend fun selectNothing() {
        val value = dataEntries.value
        value?.filter { it.selected }?.forEach {
            it.selected = false
        }
        dataEntries.postValue(dataEntries.value)
    }

    @ExperimentalCoroutinesApi
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

    fun addImportedEntries(entryList: List<Pair<Double?, Double?>>) {
//        val max = entryList.map { it.size }.maxOrNull()
//        val newEntries = ArrayList<SheetEntry?>()
//        max?.let {
//            for (currentEntryList in entryList){
//                for (i in 0 until max){
//                    try {
//                        val currentDouble = currentEntryList[i]
//                        val entry = SheetEntry(-1,-1,-1,-1, BigDecimal(currentDouble?:0.0))
//                        newEntries.add(entry)
//                    }catch (t: Throwable){
//                        newEntries.add(null)
//                    }
//                }
//            }
//        }
//        _importEntries.postValue(newEntries)
    }
}