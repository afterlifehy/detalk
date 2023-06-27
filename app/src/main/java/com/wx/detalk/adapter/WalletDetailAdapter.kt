package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.bean.SettingBean
import com.wx.common.bean.WalletDetailBean
import com.wx.common.util.GlideUtils
import com.wx.detalk.databinding.ItemSettingBinding
import com.wx.detalk.databinding.ItemWalletDetailBinding

/**
 * Created by huy  on 2023/6/15.
 */
class WalletDetailAdapter(data: MutableList<WalletDetailBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<WalletDetailBean, ItemWalletDetailBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemWalletDetailBinding>, item: WalletDetailBean) {
        GlideUtils.instance?.loadImage(holder.vb.ivLogo, item.icon)
        holder.vb.tvName.text = item.content
        holder.vb.llWalletDetail.tag = item
        holder.vb.llWalletDetail.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemWalletDetailBinding {
        return ItemWalletDetailBinding.inflate(inflater)
    }
}