package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wx.common.bean.GuideBean
import com.wx.common.util.GlideUtils
import com.wx.detalk.R
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by huy  on 2023/2/23.
 */
class BannerGuideAdapter(datas: MutableList<GuideBean>?) :
    BannerAdapter<GuideBean, BannerGuideAdapter.GuideHolder>(datas) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): GuideHolder {
        return GuideHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_guide, parent, false))
    }

    override fun onBindView(holder: GuideHolder, data: GuideBean, position: Int, size: Int) {
        GlideUtils.instance?.loadImage(holder.ivGuide, data.img)
        holder.tvDesc.text = data.txt
        holder.tvDesc2.text = data.txt2
    }

    class GuideHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivGuide: ImageView = view.findViewById(R.id.iv_guide)
        var tvDesc: TextView = view.findViewById(R.id.tv_desc)
        var tvDesc2: TextView = view.findViewById(R.id.tv_desc2)

    }
}