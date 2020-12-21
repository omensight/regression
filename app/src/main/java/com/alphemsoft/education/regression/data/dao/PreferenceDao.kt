package com.alphemsoft.education.regression.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphemsoft.education.regression.data.model.PreferenceModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenceDao: BaseDao<PreferenceModel>{

    @Query("""
        SELECT * FROM preferences WHERE preference_id = 0
    """)
    fun findUniquePreferenceFlow(): Flow<PreferenceModel>

    @Query("""
        SELECT * FROM preferences WHERE preference_id = 0
    """)
    fun findUniquePreference(): PreferenceModel
}