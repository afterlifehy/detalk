package com.wx.detalk.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.wx.base.dialog.VBBaseLibDialog
import com.wx.base.help.ActivityCacheManager
import com.wx.detalk.R
import com.wx.detalk.databinding.DialogExportSeedPhraseBinding

/**
 * Created by huy  on 2023/6/26.
 */
class ExportSeedPhraseDialog(val callback: ExportSeedPhraseCallBack) :
    VBBaseLibDialog<DialogExportSeedPhraseBinding>(ActivityCacheManager.instance().getCurrentActivity()!!), OnClickListener {

    init {
        initView()
    }

    private fun initView() {
        binding.rtvCancel.setOnClickListener(this)
        binding.rtvConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_cancel -> {
                dismiss()
            }

            R.id.rtv_confirm -> {
                callback.confirm(binding.etPw.text.toString())
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogExportSeedPhraseBinding.inflate(layoutInflater)
    }

    override fun getHideInput(): Boolean {
        return true
    }

    override fun getWidth(): Float {
        return ScreenUtils.getScreenWidth() * 0.9f
    }

    override fun getHeight(): Float {
        return WindowManager.LayoutParams.WRAP_CONTENT.toFloat()
    }

    override fun getCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    interface ExportSeedPhraseCallBack {
        fun confirm(pw: String)
    }
}