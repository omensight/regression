package com.alphemsoft.education.regression.data.model

import androidx.room.Ignore


abstract class DbEntity(
    @Ignore
    var entityId: Any
){

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return entityId.hashCode()
    }
}