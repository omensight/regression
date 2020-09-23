package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetColumnLocalDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
class DataSheetViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val dataPointDataSource: IDataPointLocalDataSource,
    private val sheetColumnLocalDataSource: ISheetColumnLocalDataSource,
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val sheetColumns: MutableLiveData<List<QuerySheetDataColumn>> = MutableLiveData()

    fun getAllSheets(): Flow<PagingData<Sheet>> {
        return sheetDataSource.findAll().flow.cachedIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    suspend fun getSheetDataColumnsPointList(sheetId: Long): Flow<List<QuerySheetDataColumn>> {
        val sheetColumns = ArrayList<QuerySheetDataColumn>(sheetColumnLocalDataSource.getSheetDataColumns(sheetId))
        this.sheetColumns.value = ArrayList<QuerySheetDataColumn>().apply {
            addAll(sheetColumns)
        }
        return this.sheetColumns.asFlow()
    }

    @ExperimentalCoroutinesApi
    suspend fun sweepAll() {
        val value = ArrayList(sheetColumns.value)
        value.forEach {
            it.dataEntries.forEach {dataEntry->
                dataEntry.data = null
            }
        }
        sheetColumns.value = ArrayList<QuerySheetDataColumn>().apply {
            addAll(value.filter { true })
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun deleteSelected() {
        val value = ArrayList(sheetColumns.value)
        value.forEach {

        }
        TODO("Feature temporary not working")
//        sheetColumns.value = value
    }

    @ExperimentalCoroutinesApi
    suspend fun selectNothing() {
        val value = sheetColumns.value
        TODO("Feature temporary not working")
//        sheetColumns.value = QuerySheetArrayList().apply {
//            addAll(sheetColumns.value)
//        }
    }

    @ExperimentalCoroutinesApi
    suspend fun selectAll() {
        val value = ArrayList(sheetColumns.value)
        TODO("Feature temporary not working")
        sheetColumns.value = java.util.ArrayList<QuerySheetDataColumn>().apply {
            addAll(value)
        }
    }

    suspend fun addTemporaryRows(numberOfEmptyDataPoints: Int, sheetId: Long) {
        val newValue = ArrayList<QuerySheetDataColumn>()
        sheetColumns.value?.let { safeSheetColumns->
            val newSheetColumns = safeSheetColumns.map {currentSheetColumn->
                val newEntries = ArrayList(currentSheetColumn.dataEntries.map {
                    it.copy(uuid = UUID.randomUUID().toString())
                })
                val augment = ArrayList<DataEntry>()
                for (i in 0 until numberOfEmptyDataPoints){
                    val dataEntry = DataEntry(0, currentSheetColumn.sheetColumn.fkSheetId)
                    dataEntry.uuid = UUID.randomUUID().toString()
                    augment.add(dataEntry)
                }
                newEntries.addAll(augment)
                currentSheetColumn.copy(dataEntries = newEntries)
            }
            newValue.addAll(newSheetColumns)
        }
        sheetColumns.postValue(newValue)

    }

    suspend fun validateAndSaveData(): Boolean {
        return false
//        val value = sheetColumns.value
//        val firstNullData = value.firstOrNull { dataPoint ->
//            dataPoint.x == null || dataPoint.y == null
//        }
//        return firstNullData?.let {
//            false
//        } ?: run {
//            val existingEntities = value.filter {
//                it.id != 0L
//            }
//            val newEntities = value.filter {
//                it.id == 0L
//            }
//            dataPointDataSource.insert(newEntities)
//            dataPointDataSource.update(existingEntities)
//            true
//        }
    }
}