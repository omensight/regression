package com.alphemsoft.education.regression.ui.viewholder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.IPreferenceDataSource
import com.alphemsoft.education.regression.data.model.PreferenceModel

class PreferenceViewModel @ViewModelInject constructor(private val preferenceDataSource: IPreferenceDataSource): ViewModel(){

    val preferenceFlow = preferenceDataSource.findUniqueDataStoreFlow()

    suspend fun update(preferenceModel: PreferenceModel){
        preferenceDataSource.find(0)?.let {
            preferenceDataSource.update(listOf(preferenceModel))
        }?: kotlin.run {
            preferenceDataSource.insert(preferenceModel)
        }
    }
}