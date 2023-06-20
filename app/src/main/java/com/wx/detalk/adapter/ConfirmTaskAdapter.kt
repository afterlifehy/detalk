package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.bean.ConfirmBean
import com.wx.detalk.databinding.ItemConfirmInfoBinding

/**
 * Created by huy  on 2023/6/16.
 */
class ConfirmTaskAdapter(data: MutableList<ConfirmBean>? = null) :
    BaseBindingAdapter<ConfirmBean, ItemConfirmInfoBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemConfirmInfoBinding>, item: ConfirmBean) {
        holder.vb.tvTopic.text = item.topic
        holder.vb.tvContent.text = item.content
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemConfirmInfoBinding {
        return ItemConfirmInfoBinding.inflate(inflater)
    }
}