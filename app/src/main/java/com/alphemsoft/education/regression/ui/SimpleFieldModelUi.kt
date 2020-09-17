package com.alphemsoft.education.regression.ui

data class SimpleFieldModelUi(
    val title: String,
    val hint: String,
    val description: String,
    var value: String? = null,
) {
}