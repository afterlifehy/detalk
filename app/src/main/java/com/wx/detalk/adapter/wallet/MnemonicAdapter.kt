package com.wx.detalk.adapter.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.detalk.R
import com.wx.detalk.databinding.ItemMnemonicBinding

/**
 * Created by huy  on 2022/8/19.
 */
class MnemonicAdapter(data: MutableList<String>? = null, var isShow: Boolean) :
    BaseBindingAdapter<String, ItemMnemonicBinding>(data) {
    var count = 1

    override fun convert(holder: VBViewHolder<ItemMnemonicBinding>, item: String) {
        var countString = ""
        val index = data.indexOf(item) + 1
        if (index < 10) {
            countString = "0${index}"
        } else {
            countString = index.toString()
        }
        holder.setText(R.id.tv_count, countString)
        holder.setText(R.id.tv_mnemonic, item)
        if (isShow) {
            holder.vb.tvMnemonic.show()
        } else {
            holder.vb.tvMnemonic.gone()
        }
    }

    fun setIsShow(isShow: Boolean) {
        this.isShow = isShow
        count = 1
        notifyDataSetChanged()
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemMnemonicBinding {
        return ItemMnemonicBinding.inflate(inflater)
    }
}