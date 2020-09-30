package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.databinding.ItemImportPairBinding
import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.viewholder.ImportPairViewHolder
import java.math.BigDecimal

class ImportPairAdapter: BaseEntityAdapter<Pair<BigDecimal, BigDecimal>, ImportPairViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ImportPairViewHolder {
        val binding = ItemImportPairBinding.inflate(inflater, parent, false)
        return ImportPairViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImportPairViewHolder, position: Int) {
        holder.bind(items[position])
    }
}