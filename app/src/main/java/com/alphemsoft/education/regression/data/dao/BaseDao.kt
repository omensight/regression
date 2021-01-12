package com.alphemsoft.education.regression.data.dao

import androidx.room.*

@Dao
interface BaseDao<in T> {

    @Insert
    fun insert(item: T): Long

    @Insert
    fun insert(vararg item: T): List<Long>

    @Insert
    @Transaction
    fun insert(items: List<T>)

    @Update
    fun update(vararg item: T)

    @Update
    fun update(items: List<T>)

    @Delete
    fun delete(delete: List<T>)

    @Delete
    fun delete(vararg delete: T)

}