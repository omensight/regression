package com.alphemsoft.education.regression.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.Sheet

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
}