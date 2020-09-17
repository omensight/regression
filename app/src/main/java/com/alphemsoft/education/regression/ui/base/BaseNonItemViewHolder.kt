package com.alphemsoft.education.regression.ui.base

import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseNonItemViewHolder<VB : ViewBinding>(
    protected val mDataBinding: VB,
    protected val navController: NavController? = null
) :
    RecyclerView.ViewHolder(mDataBinding.root) {
    protected val context = mDataBinding.root.context
    abstract fun bind()
}