package com.alphemsoft.education.regression.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.DataPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface DataPointDao: BaseDao<DataPoint> {
    @Query("""
        SELECT data_points.* FROM data_points
        WHERE fk_sheet_id = :sheetId
    """)
    fun findSheetDataPointSource(sheetId: Long): PagingSource<Int, DataPoint>

    @Query("""
        SELECT data_points.* FROM data_points
        WHERE fk_sheet_id = :sheetId
    """)
    suspend fun findSheetDataPoints(sheetId: Long): List<DataPoint>

    @Query("""
        SELECT data_points.* from data_points 
        WHERE data_point_id = :dataPointId
    """)
    fun findById(dataPointId: Long): DataPoint

    @Query("""
        SELECT data_points.* from data_points 
        WHERE fk_sheet_id = :sheetId
    """)
    fun findDataPointsFlowById(sheetId: Long): Flow<List<DataPoint>>
}