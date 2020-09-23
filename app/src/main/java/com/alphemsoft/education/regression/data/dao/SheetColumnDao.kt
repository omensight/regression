package com.alphemsoft.education.regression.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.SheetColumn
import kotlinx.coroutines.flow.Flow

@Dao
interface SheetColumnDao: BaseDao<SheetColumn> {

    @Query("""
        SELECT * FROM sheet_columns
        WHERE sheet_column_id = :id
    """)
    suspend fun findById(id: Long): SheetColumn

    @Query("""
        SELECT * FROM sheet_columns
        WHERE sheet_column_fk_sheet_id = :sheetId
    """)
    suspend fun findSheetColumns(sheetId: Long): List<QuerySheetDataColumn>

    @Query("""
        SELECT * FROM sheet_columns
        WHERE sheet_column_fk_sheet_id = :sheetId
    """)
    fun findSheetColumnsFlow(sheetId: Long): Flow<List<QuerySheetDataColumn>>
}