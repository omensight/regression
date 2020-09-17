package com.alphemsoft.education.regression.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CoroutineHandler(private val jobParent: Job) {
    val backgroundScope = CoroutineScope(jobParent + Dispatchers.Default)
    val foregroundScope = CoroutineScope(jobParent + Dispatchers.Main)
    val ioScope = CoroutineScope(jobParent + Dispatchers.IO)

    fun cancel(){
        jobParent.cancel()
    }
}