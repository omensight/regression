package com.alphemsoft.education.regression.data.model.secondary

import androidx.annotation.StringRes
import kotlin.random.Random

data class Result(
    @StringRes val title: Int,
    val formula: String,
    val value: Double?,
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (title != other.title) return false
        if (formula != other.formula) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title
        result = 31 * result + formula.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}