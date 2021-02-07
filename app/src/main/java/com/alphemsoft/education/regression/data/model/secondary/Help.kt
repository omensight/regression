package com.alphemsoft.education.regression.data.model.secondary

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Help(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val description: Int? = null
)