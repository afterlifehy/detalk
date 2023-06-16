package com.wx.detalk.ui.dialog

import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.wx.base.dialog.VBBaseLibDialog
import com.wx.base.help.ActivityCacheManager
import com.wx.detalk.R
import com.wx.detalk.databinding.DialogCreateWalletBinding

/**
 * Created by huy  on 2023/6/15.
 */
class CreateWalletDialog(val callback: CreateWalletCallBack) :
    VBBaseLibDialog<DialogCreateWalletBinding>(
        ActivityCacheManager.instance().getCurrentActivity()!!,
        com.wx.base.R.style.CommonBottomDialogStyle
    ), OnClickListener {

    init {
        initView()
    }

    private fun initView() {
        binding.ivClose.setOnClickListener(this)
        binding.rrlCreateWallet.setOnClickListener(this)
        binding.rrlImportWallet.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }

            R.id.rrl_createWallet -> {
                callback.createWallet()
                dismiss()
            }

            R.id.rrl_importWallet -> {
                callback.importWallet()
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogCreateWalletBinding.inflate(layoutInflater)
    }

    override fun getHideInput(): Boolean {
        return true
    }

    override fun getWidth(): Float {
        return WindowManager.LayoutParams.MATCH_PARENT.toFloat()
    }

    override fun getHeight(): Float {
        return WindowManager.LayoutParams.WRAP_CONTENT.toFloat()
    }

    override fun getCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun getGravity(): Int {
        return Gravity.BOTTOM
    }

    interface CreateWalletCallBack {
        fun createWallet()
        fun importWallet()
    }
}