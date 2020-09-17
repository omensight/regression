package com.alphemsoft.education.regression.data.datasource.base

interface BaseDataSource<T, in K> {
    suspend fun find(id: K): T
    suspend fun insert(item: T): Long
    suspend fun delete(items: List<T>)
    suspend fun insert(items: List<T>)
    suspend fun update(items: List<T>)
}