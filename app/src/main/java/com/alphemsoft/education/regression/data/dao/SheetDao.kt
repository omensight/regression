package com.alphemsoft.education.regression.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.flow.Flow

@Dao
interface SheetDao: BaseDao<Sheet> {

    @Query("""
        SELECT sheets.* FROM sheets WHERE sheet_id = :sheetId
    """)
    suspend fun findSheetById(sheetId: Long): Sheet

    @Query("""
        SELECT * from sheets
    """)
    fun findAll(): PagingSource<Int, Sheet>

    @Query("SELECT COUNT(*) FROM sheets")
    fun getSheetCount(): Flow<Long>

    @Query("SELECT MAX(sheet_id) FROM sheets")
    suspend fun findMaxId(): Long?
}