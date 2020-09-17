package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.math.BigDecimal

class DataSheetViewModel @ViewModelInject constructor(
    private val sheetDataSource: ISheetDataSource,
    private val dataPointDataSource: IDataPointLocalDataSource,
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val dataPoints: MutableStateFlow<MutableList<DataPoint>> = MutableStateFlow(ArrayList())

    fun getAllSheets(): Flow<PagingData<Sheet>> {
        return sheetDataSource.findAll().flow.cachedIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    suspend fun getDataPointList(sheetId: Long): Flow<List<DataPoint>> {
        dataPoints.value = ArrayList(dataPointDataSource.getDataPointList(sheetId))
        return dataPoints
    }

    @ExperimentalCoroutinesApi
    suspend fun addTemporaryPoint(sheetId: Long) {
        val value = dataPoints.value + DataPoint(id = 0, fkSheetId = sheetId, null, null, false)
        dataPoints.value = ArrayList(value)
    }

    @ExperimentalCoroutinesApi
    suspend fun sweepAll() {
        val value = ArrayList<DataPoint>(dataPoints.value)
        value.forEach {
            it.x = null
            it.y = null
        }
        dataPoints.value = ArrayList(value.filter { true })
    }

    @ExperimentalCoroutinesApi
    suspend fun deleteSelected() {
        val value = ArrayList(dataPoints.value)
        value.removeAll { it.selected }
        dataPoints.value = value
    }

    @ExperimentalCoroutinesApi
    suspend fun selectNothing() {
        val value = dataPoints.value
        value.filter { it.selected }.forEach {
            it.selected = false
        }
        dataPoints.value = ArrayList(dataPoints.value)
    }

    @ExperimentalCoroutinesApi
    suspend fun selectAll() {
        val value = ArrayList(dataPoints.value)
        value.forEach {
            it.selected = true
        }
        dataPoints.value = value
    }

    suspend fun addTemporaryPoints(numberOfEmptyDataPoints: Int, sheetId: Long) {
        val value = ArrayList(dataPoints.value)
        for (i in 0 until numberOfEmptyDataPoints) {
            value.add(DataPoint(0, sheetId, null, null, false))
        }
        dataPoints.value = value
    }

    suspend fun validateAndSaveData(): Boolean {
        val value = dataPoints.value
        val firstNullData = value.firstOrNull { dataPoint ->
            dataPoint.x == null || dataPoint.y == null
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
}