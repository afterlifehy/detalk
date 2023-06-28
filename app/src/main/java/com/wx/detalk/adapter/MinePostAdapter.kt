package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.util.GlideUtils
import com.wx.detalk.databinding.ItemPostBinding

/**
 * Created by huy  on 2023/6/27.
 */
class MinePostAdapter(data: MutableList<Int>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<Int, ItemPostBinding>(data) {


    override fun convert(holder: VBViewHolder<ItemPostBinding>, item: Int) {
        GlideUtils.instance?.loadImage(holder.vb.rivHead, com.wx.common.R.mipmap.ic_test_head)
        holder.vb.tvName.text = "BAYC to the MOON"
        holder.vb.tvTime.text = "16 mins ago"
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

        holder.vb.cbPraise.setOnClickListener(onClickListener)
        holder.vb.tvComment.setOnClickListener(onClickListener)
        holder.vb.cbLove.setOnClickListener(onClickListener)
        holder.vb.ivReward.setOnClickListener(onClickListener)
        holder.vb.ivShare.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPostBinding {
        return ItemPostBinding.inflate(inflater)
    }
}