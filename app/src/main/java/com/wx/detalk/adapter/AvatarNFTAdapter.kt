package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.databinding.ItemAvatarNftBinding

/**
 * Created by huy  on 2023/6/27.
 */
class AvatarNFTAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemAvatarNftBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemAvatarNftBinding>, item: Int) {
        holder.vb.tvName.text = "David"

        holder.vb.rlNft.tag = item
        holder.vb.rlNft.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemAvatarNftBinding {
        return ItemAvatarNftBinding.inflate(inflater)
    }
}