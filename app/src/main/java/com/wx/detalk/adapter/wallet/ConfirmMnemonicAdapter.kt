package com.wx.detalk.adapter.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.wx.base.BaseApplication
import com.wx.base.adapter.BaseBindingAdapter
import com.wx.base.adapter.VBViewHolder
import com.wx.detalk.R
import com.wx.detalk.databinding.ItemConfirmMnemonicBinding

/**
 * Created by huy  on 2022/8/19.
 */
class ConfirmMnemonicAdapter(
    data: MutableList<String>? = null,
    val onClickListener: View.OnClickListener
) :
    BaseBindingAdapter<String, ItemConfirmMnemonicBinding>(data) {

    override fun convert(holder: VBViewHolder<ItemConfirmMnemonicBinding>, item: String) {
        val rtv_confirmMnemonic = holder.vb.rtvConfirmMnemonic
        if (item.isEmpty()) {
            rtv_confirmMnemonic.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.transparent
                )
            )
            rtv_confirmMnemonic.delegate.setStrokeColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.transparent
                )
            )
            rtv_confirmMnemonic.delegate.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.transparent
                )
            )
        } else {
            rtv_confirmMnemonic.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.color_ff141414
                )
            )
            rtv_confirmMnemonic.delegate.setTextColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.wx.base.R.color.white
                )
            )
        }
        rtv_confirmMnemonic.delegate.init()
        holder.setText(R.id.rtv_confirmMnemonic, item)
        rtv_confirmMnemonic.tag = item
        rtv_confirmMnemonic.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemConfirmMnemonicBinding {
        return ItemConfirmMnemonicBinding.inflate(inflater)
    }

}