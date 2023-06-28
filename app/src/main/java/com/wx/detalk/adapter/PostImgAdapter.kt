package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.util.GlideUtils
import com.wx.detalk.databinding.ItemPostImgBinding

/**
 * Created by huy  on 2023/6/27.
 */
class PostImgAdapter(data: MutableList<Int>? = null,val onClickListener: OnClickListener) : BaseBindingAdapter<Int, ItemPostImgBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemPostImgBinding>, item: Int) {
        GlideUtils.instance?.loadImage(holder.vb.rivPostImg, com.wx.common.R.mipmap.ic_test_head)
        holder.vb.rivPostImg.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPostImgBinding {
        val binding = ItemPostImgBinding.inflate(inflater)
//        val lp = binding.rivPostImg.layoutParams
//        lp.height = lp.width
//        ItemPostImgBinding.inflate(inflater).rivPostImg.layoutParams = lp
        return binding
    }
}