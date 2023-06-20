package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wx.common.util.GlideUtils
import com.wx.detalk.R
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by huy  on 2023/2/23.
 */
class BannerAdAdapter(datas: MutableList<String>?) : BannerAdapter<String, BannerAdAdapter.AdHolder>(datas) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): AdHolder {
        return AdHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_banner_ad, parent, false))
    }

    override fun onBindView(holder: AdHolder?, data: String?, position: Int, size: Int) {
        GlideUtils.instance?.loadImage(holder?.ivAd, data)
    }

    class AdHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivAd: ImageView

        init {
            ivAd = view.findViewById(R.id.iv_ad)
        }
    }
}