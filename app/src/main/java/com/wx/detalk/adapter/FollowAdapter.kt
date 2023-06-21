package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.base.util.Constant
import com.wx.detalk.databinding.ItemFollowBinding
import i18n

/**
 * Created by huy  on 2023/6/21.
 */
class FollowAdapter(data: MutableList<Int>? = null, val followType: Int, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemFollowBinding>(data) {

    override fun convert(holder: VBViewHolder<ItemFollowBinding>, item: Int) {
        when (followType) {
            Constant.FRIENDS -> {
                holder.vb.rtvFollowStatus.text = i18n(com.wx.base.R.string.Friends)
            }

            Constant.FOLLOWING -> {
                holder.vb.rtvFollowStatus.text = i18n(com.wx.base.R.string.Following)
            }

            Constant.FOLLOWER -> {
                holder.vb.rtvFollowStatus.text = i18n(com.wx.base.R.string.Followers)
            }
        }
        holder.vb.rtvFollowStatus.tag = item
        holder.vb.rtvFollowStatus.setOnClickListener(onClickListener)

        holder.vb.rrlFollow.tag = item
        holder.vb.rrlFollow.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFollowBinding {
        return ItemFollowBinding.inflate(inflater)
    }
}