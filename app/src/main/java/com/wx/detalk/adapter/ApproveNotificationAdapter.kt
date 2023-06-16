package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.databinding.ItemApproveNotificationBinding

/**
 * Created by huy  on 2023/6/15.
 */
class ApproveNotificationAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemApproveNotificationBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemApproveNotificationBinding>, item: Int) {
        holder.vb.rtvApprove.tag = item
        holder.vb.rtvApprove.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemApproveNotificationBinding {
        return ItemApproveNotificationBinding.inflate(inflater)
    }
}