package com.wx.detalk.adapter.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.wx.base.BaseApplication
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.R
import com.wx.detalk.databinding.ItemRandomMnemonicBinding

/**
 * Created by huy  on 2022/8/19.
 */
class RandomMnemonicAdapter(
    data: MutableList<String>? = null,
    val onClickListener: View.OnClickListener
) :
    BaseBindingAdapter<String, ItemRandomMnemonicBinding>(data) {
    var mnemonicCheckedList: BooleanArray? = null

    override fun convert(holder: VBViewHolder<ItemRandomMnemonicBinding>, item: String) {
        val rtv_randomMnemonic = holder.vb.rtvRandomMnemonic
        if (mnemonicCheckedList!![data.indexOf(item)]) {
            rtv_randomMnemonic.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.transparent
                )
            )
            rtv_randomMnemonic.delegate.setStrokeColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.transparent
                )
            )
            rtv_randomMnemonic.delegate.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.transparent
                )
            )
        } else {
            rtv_randomMnemonic.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.color_ff141414
                )
            )
            rtv_randomMnemonic.delegate.setStrokeColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.color_ff434343
                )
            )
            rtv_randomMnemonic.delegate.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.white
                )
            )
        }
        rtv_randomMnemonic.delegate.init()
        holder.setText(R.id.rtv_randomMnemonic, item)
        rtv_randomMnemonic.tag = item
        rtv_randomMnemonic.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRandomMnemonicBinding {
        return ItemRandomMnemonicBinding.inflate(inflater)
    }

    fun setCheckedList(mnemonicCheckedList: BooleanArray) {
        this.mnemonicCheckedList = mnemonicCheckedList
    }
}