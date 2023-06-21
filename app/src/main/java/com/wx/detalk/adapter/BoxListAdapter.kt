package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.base.ext.gone
import com.wx.base.ext.hide
import com.wx.base.ext.show
import com.wx.detalk.databinding.ItemBoxBinding
import i18n

/**
 * Created by huy  on 2023/6/14.
 */
class BoxListAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemBoxBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemBoxBinding>, item: Int) {
        holder.vb.tvBoxName.text = "RunBa NFT"
        holder.vb.tvBoxDesc.text = "Runba is a game.Runba is a game.Runba is a game."
        when (item) {
            0 -> {
                holder.vb.tvType.show()
                holder.vb.rflNotification.hide()
                holder.vb.tvType.text = i18n(com.wx.base.R.string.DAO)
            }

            1 -> {
                holder.vb.tvType.show()
                holder.vb.rflNotification.hide()
                holder.vb.tvType.text = i18n(com.wx.base.R.string.CLUB)
            }

            2 -> {
                holder.vb.rflNotification.show()
                holder.vb.tvType.hide()
                holder.vb.tvBoxName.text = i18n(com.wx.base.R.string.Notification)
                holder.vb.rivBoxLogo.gone()
                holder.vb.ivNoticification.show()
            }
        }
        holder.vb.rrlBox.tag = item
        holder.vb.rrlBox.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemBoxBinding {
        return ItemBoxBinding.inflate(inflater)
    }
}