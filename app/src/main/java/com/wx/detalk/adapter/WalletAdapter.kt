package com.wx.detalk.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.common.bean.LocalWalletBean
import com.wx.detalk.databinding.ItemWalletBinding

/**
 * Created by huy  on 2023/6/21.
 */
class WalletAdapter(
    data: MutableList<LocalWalletBean>? = null,
    var currentWallet: LocalWalletBean?,
    val onClickListener: OnClickListener
) :
    BaseBindingAdapter<LocalWalletBean, ItemWalletBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemWalletBinding>, item: LocalWalletBean) {
        holder.vb.tvAddress.text = item.address
        if (currentWallet != null && item.address == currentWallet!!.address) {
            holder.vb.ivWalletChecked.show()
        } else {
            holder.vb.ivWalletChecked.gone()
        }
        holder.vb.tvCopy.tag = item
        holder.vb.tvCopy.setOnClickListener(onClickListener)
        holder.vb.rflWallet.tag = item
        holder.vb.rflWallet.setOnClickListener(onClickListener)
        holder.vb.tvName.tag = item
        holder.vb.tvName.setOnClickListener(onClickListener)
        holder.vb.tvAddress.tag = item
        holder.vb.tvAddress.setOnClickListener(onClickListener)
        holder.vb.rrlWallet.tag = item
        holder.vb.rrlWallet.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemWalletBinding {
        return ItemWalletBinding.inflate(inflater)
    }

}