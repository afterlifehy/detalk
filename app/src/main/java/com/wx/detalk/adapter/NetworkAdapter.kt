package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.bean.NetworkBean
import com.wx.common.util.GlideUtils
import com.wx.detalk.databinding.ItemNetworkBinding

/**
 * Created by huy  on 2023/6/15.
 */
class NetworkAdapter(data: MutableList<NetworkBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<NetworkBean, ItemNetworkBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemNetworkBinding>, item: NetworkBean) {
        GlideUtils.instance?.loadImage(holder.vb.ivLogo, item.logo)
        holder.vb.tvName.text = item.name
        holder.vb.llNetwork.tag = item
        holder.vb.llNetwork.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemNetworkBinding {
        return ItemNetworkBinding.inflate(inflater)
    }
}