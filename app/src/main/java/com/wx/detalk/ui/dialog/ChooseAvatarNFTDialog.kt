package com.wx.detalk.ui.dialog

import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.wx.base.BaseApplication
import com.wx.base.dialog.VBBaseLibDialog
import com.wx.base.help.ActivityCacheManager
import com.wx.detalk.R
import com.wx.detalk.adapter.AvatarNFTAdapter
import com.wx.detalk.databinding.DialogChooseAvatarNftBinding

/**
 * Created by huy  on 2023/6/27.
 */
class ChooseAvatarNFTDialog(val callback: ChooseAvatarNFTCallBack) :
    VBBaseLibDialog<DialogChooseAvatarNftBinding>(
        ActivityCacheManager.instance().getCurrentActivity()!!,
        com.wx.base.R.style.CommonBottomDialogStyle
    ), OnClickListener {
    var avatarNFTAdapter: AvatarNFTAdapter? = null
    var avatarNFTList: MutableList<Int> = ArrayList()

    init {
        initView()
    }

    private fun initView() {
        avatarNFTList.add(0)
        avatarNFTList.add(0)
        avatarNFTList.add(0)
        avatarNFTList.add(0)
        binding.rvChooseNFT.setHasFixedSize(true)
        binding.rvChooseNFT.layoutManager = LinearLayoutManager(BaseApplication.instance())
        avatarNFTAdapter = AvatarNFTAdapter(avatarNFTList, this)
        binding.rvChooseNFT.adapter = avatarNFTAdapter

        binding.ivClose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }

            R.id.rl_avatar -> {
                callback.chooseAvatarNFT("")
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding? {
        return DialogChooseAvatarNftBinding.inflate(layoutInflater)
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

    interface ChooseAvatarNFTCallBack {
        fun chooseAvatarNFT(avatarNFT: String)
    }
}