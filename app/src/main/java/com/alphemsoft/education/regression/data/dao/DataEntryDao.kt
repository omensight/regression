package com.alphemsoft.education.regression.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.DataEntry

@Dao
interface DataEntryDao: BaseDao<DataEntry> {

    @Query("""
        SELECT data_entries.* from data_entries 
        WHERE data_entry_id = :dataEntryId
    """)
    fun findById(dataEntryId: Long): DataEntry
}