package com.alphemsoft.education.regression.ui.base

import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.viewholder.Selectable
import kotlinx.coroutines.Job

abstract class BaseItemViewHolder< VB : ViewDataBinding, T>(
    protected val mDataBinding: VB,
    protected val navController: NavController? = null
) : RecyclerView.ViewHolder(mDataBinding.root), Selectable {
    protected val coroutineHandler = CoroutineHandler(Job())
    protected val context = mDataBinding.root.context
    abstract fun bind(item: T?)
}