package com.wx.detalk.adapter.wallet

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.databinding.ItemMineDaoClubBinding

/**
 * Created by huy  on 2023/6/20.
 */
class MineDaoClubAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemMineDaoClubBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemMineDaoClubBinding>, item: Int) {
        holder.vb.rtvSet.tag = item
        holder.vb.rtvSet.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemMineDaoClubBinding {
        return ItemMineDaoClubBinding.inflate(inflater)
    }
}