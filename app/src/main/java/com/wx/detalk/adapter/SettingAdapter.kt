package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.bean.SettingBean
import com.wx.common.util.GlideUtils
import com.wx.detalk.databinding.ItemSettingBinding

/**
 * Created by huy  on 2023/6/15.
 */
class SettingAdapter(data: MutableList<SettingBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<SettingBean, ItemSettingBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemSettingBinding>, item: SettingBean) {
        GlideUtils.instance?.loadImage(holder.vb.ivLogo, item.icon)
        holder.vb.tvName.text = item.content
        holder.vb.llSetting.tag = item
        holder.vb.llSetting.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemSettingBinding {
        return ItemSettingBinding.inflate(inflater)
    }
}