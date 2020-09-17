package com.alphemsoft.education.regression.ui.base

import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.viewholder.Selectable
import kotlinx.coroutines.Job

abstract class BaseItemViewHolder< VB : ViewDataBinding, T>(
    protected val mViewBinding: VB,
    protected val navController: NavController? = null
) : RecyclerView.ViewHolder(mViewBinding.root), Selectable {
    protected val coroutineHandler = CoroutineHandler(Job())
    protected val context = mViewBinding.root.context
    abstract fun bind(item: T?)
}