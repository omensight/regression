package com.alphemsoft.education.regression.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.ISubscriptionDataSource

class ShowExportResultViewModel @ViewModelInject constructor(
    private val subscriptionDataSource: ISubscriptionDataSource
):ViewModel() {
    val exportUriLiveData = MutableLiveData<Uri>()

    val exportFileNameLiveData = MutableLiveData<String>()

    suspend fun hasPremiumSubscription(): Boolean {
        val subscription = subscriptionDataSource.getUniqueSubscription()
        return subscription?.hasAnActiveSubscription()?:false
    }

    fun initialize(name: String, uri: Uri) {
        exportFileNameLiveData.value = name
        exportUriLiveData.value = uri
    }
}
