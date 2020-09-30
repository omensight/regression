package com.alphemsoft.education.regression.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.SheetEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DataPointDao: BaseDao<SheetEntry> {
    @Query("""
        SELECT sheet_entries.* FROM sheet_entries
        WHERE fk_sheet_id = :sheetId
    """)
    fun findSheetDataPointSource(sheetId: Long): PagingSource<Int, SheetEntry>

    @Query("""
        SELECT sheet_entries.* FROM sheet_entries
        WHERE fk_sheet_id = :sheetId
    """)
    suspend fun findSheetDataPoints(sheetId: Long): List<SheetEntry>

    @Query("""
        SELECT sheet_entries.* from sheet_entries 
        WHERE sheet_entry_id = :dataPointId
    """)
    fun findById(dataPointId: Long): SheetEntry

    @Query("""
        SELECT sheet_entries.* from sheet_entries 
        WHERE fk_sheet_id = :sheetId
    """)
    fun findDataPointsFlowById(sheetId: Long): Flow<List<SheetEntry>>
}