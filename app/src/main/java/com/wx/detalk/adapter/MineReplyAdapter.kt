package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.util.AppUtil
import com.wx.common.util.GlideUtils
import com.wx.detalk.databinding.ItemReplyBinding

/**
 * Created by huy  on 2023/6/27.
 */
class MineReplyAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemReplyBinding>(data) {
    val colors = intArrayOf(com.wx.base.R.color.color_ffffee00, com.wx.base.R.color.white)
    val sizes = intArrayOf(14, 14)

    override fun convert(holder: VBViewHolder<ItemReplyBinding>, item: Int) {
        GlideUtils.instance?.loadImage(holder.vb.rivHead, com.wx.common.R.mipmap.ic_test_head)
        val strings = arrayOf("David001:", "Runba is a gamefi app released on No joined club. You can")
        holder.vb.tvReply.text = AppUtil.getSpan(strings, sizes, colors)
        holder.vb.tvTime.text = "16 mins ago"

        GlideUtils.instance?.loadImage(holder.vb.rivOriginHead, com.wx.common.R.mipmap.ic_test_head)
        holder.vb.tvName.text = "BAYC to the MOON"
        holder.vb.tvOriginTime.text = "16 mins ago"
        holder.vb.tvContent.text =
            "Runba is a gamefi app released on No joined club. You can access the clubf Runba is a gamefi app released on No joined club. You can access the clubf"

        holder.vb.rvImg.setHasFixedSize(true)
        holder.vb.rvImg.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val postImgList: MutableList<Int> = ArrayList()
        for (i in 0..item) {
            postImgList.add(0)
        }
        val postImgAdapter = PostImgAdapter(postImgList, onClickListener)
        holder.vb.rvImg.adapter = postImgAdapter

    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemReplyBinding {
        return ItemReplyBinding.inflate(inflater)
    }
}