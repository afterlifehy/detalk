package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.databinding.ItemDaoClubBinding

/**
 * Created by huy  on 2023/6/16.
 */
class DaoClubAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemDaoClubBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemDaoClubBinding>, item: Int) {
        holder.vb.rllDaoClub.tag = item
        holder.vb.rllDaoClub.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemDaoClubBinding {
        return ItemDaoClubBinding.inflate(inflater)
    }
}