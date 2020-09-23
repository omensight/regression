package com.alphemsoft.education.regression.ui.base

import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.data.model.DbEntity

abstract class BaseEntityAdapter<T: DbEntity, VH: RecyclerView.ViewHolder>: BaseAdapter<T,VH>() {

}