package com.alphemsoft.education.regression.ui.customview

import android.content.Context
import androidx.appcompat.app.AlertDialog

class QuestionDialog(
    private val context: Context,
    private val title: Int,
    private val message: Int,
    private val positiveAction: () -> Unit,
    private val negativeAction: () -> Unit = {},
    private val positiveText: String = context.getString(android.R.string.ok),
    private val negativeText: String = context.getString(android.R.string.cancel)
) {
    private lateinit var alertDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveText) { _, _ ->
                positiveAction.invoke()
            }
            setNegativeButton(negativeText) { _, _ ->
                alertDialog.dismiss()
            }
        }
        alertDialog = builder.create()
    }

    fun show(){
        alertDialog.show()
    }
}