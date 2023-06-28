package com.wx.detalk.ui.dialog

import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.wx.base.dialog.VBBaseLibDialog
import com.wx.base.help.ActivityCacheManager
import com.wx.detalk.R
import com.wx.detalk.databinding.DialogMineEditBinding

/**
 * Created by huy  on 2023/6/27.
 */
class MineEditDialog(val callback: MineEditCallBack) : VBBaseLibDialog<DialogMineEditBinding>(
    ActivityCacheManager.instance().getCurrentActivity()!!,
    com.wx.base.R.style.CommonBottomDialogStyle
), OnClickListener {
    var chooseAvatarNFTDialog: ChooseAvatarNFTDialog? = null
    var avatar = ""

    init {
        initView()
    }

    private fun initView() {
        binding.ivClose.setOnClickListener(this)
        binding.rlAvatar.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }

            R.id.rl_avatar -> {
                chooseAvatarNFTDialog = null
                chooseAvatarNFTDialog = ChooseAvatarNFTDialog(object : ChooseAvatarNFTDialog.ChooseAvatarNFTCallBack {
                    override fun chooseAvatarNFT(avatarNFT: String) {
                        avatar = avatarNFT
                    }

                })
                chooseAvatarNFTDialog?.show()
            }

            R.id.tv_save -> {
                callback.save(binding.etName.text.toString(), avatar)
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogMineEditBinding.inflate(layoutInflater)
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

    interface MineEditCallBack {
        fun save(name: String, avatar: String)
    }
}