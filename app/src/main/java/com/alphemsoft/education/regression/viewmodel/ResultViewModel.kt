package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.ISubscriptionDataSource
import com.alphemsoft.education.regression.data.datasource.SheetLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.SheetEntryLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.SubscriptionDataSource
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.Subscription
import kotlinx.coroutines.flow.Flow


class ResultViewModel @ViewModelInject constructor(
    private val dataPointLocalDataSource: SheetEntryLocalDataSource,
    private val sheetLocalDataSource: SheetLocalDataSource,
    private val subscriptionDataSource: ISubscriptionDataSource
) :ViewModel() {

    fun getDataPointsFlow(sheetId: Long): Flow<List<SheetEntry>> {
        return dataPointLocalDataSource.getDataPointsFlow(sheetId)
    }

    suspend fun getSheet(regressionId: Long): Sheet? {
        return sheetLocalDataSource.find(regressionId)
    }

    suspend fun getDataPoints(sheetId: Long): List<SheetEntry> {
        return dataPointLocalDataSource.getDataPointList(sheetId)
    }

    suspend fun hasAnActiveSubscription(): Boolean {
        return subscriptionDataSource.getUniqueSubscription()?.hasAnActiveSubscription()?:false
    }

    fun getSubscription(): Flow<Subscription>{
        return subscriptionDataSource.getUniqueSubscriptionFlow()
    }
}