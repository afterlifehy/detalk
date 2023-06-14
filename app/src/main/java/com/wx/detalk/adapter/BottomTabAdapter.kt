package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.wx.base.BaseApplication
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.common.bean.BottomTabBean
import com.wx.detalk.databinding.ItemBottomTabBinding

/**
 * Created by huy  on 2022/8/8.
 */
class BottomTabAdapter(data: MutableList<BottomTabBean>? = null, val mOnTabSelectLinsener: OnTabSelectLinsener?) :
    BaseBindingAdapter<BottomTabBean, ItemBottomTabBinding>(data) {
    var mSelectTabIndex = 0
    var oldTab = 0

    override fun convert(holder: VBViewHolder<ItemBottomTabBinding>, item: BottomTabBean) {
        holder.vb.tvTab.text = item.tabName
        val irvTab = holder.vb.irvTab
        irvTab.setCheckedImg(item.checkedImg)
        irvTab.setUnCheckedImg(item.unCheckedImg)
        irvTab.isChecked = holder.layoutPosition == mSelectTabIndex
        if (irvTab.isChecked) {
            holder.vb.tvTab.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.black
                )
            )
        } else {
            holder.vb.tvTab.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.black_30_color
                )
            )
        }
        holder.vb.llTab.setOnClickListener {
            oldTab = mSelectTabIndex
            mSelectTabIndex = holder.layoutPosition
            notifyItemChanged(mSelectTabIndex)
            notifyItemChanged(oldTab)
            mOnTabSelectLinsener?.onTabSelectLinsener(holder.layoutPosition, item)
        }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemBottomTabBinding {
        return ItemBottomTabBinding.inflate(inflater, parent, false)
    }

    interface OnTabSelectLinsener {
        fun onTabSelectLinsener(
            tabIndex: Int = 0,
            item: BottomTabBean? = null
        )
    }

    fun tabSelect(index: Int) {
        oldTab = mSelectTabIndex
        mSelectTabIndex = index
        notifyItemChanged(mSelectTabIndex)
        notifyItemChanged(oldTab)
    }
}
