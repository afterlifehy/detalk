package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.databinding.ItemLeaveNotificationBinding

/**
 * Created by huy  on 2023/6/16.
 */
class LeaveNotificationAdapter(data: MutableList<String>? = null) :
    BaseBindingAdapter<String, ItemLeaveNotificationBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemLeaveNotificationBinding>, item: String) {
        holder.vb.rtvLeave.text = item
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemLeaveNotificationBinding {
        return ItemLeaveNotificationBinding.inflate(inflater)
    }
}