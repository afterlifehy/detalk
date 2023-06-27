package com.wx.detalk.ui.activity.wallet

import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.EncryptUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.util.ToastUtil
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.LocalWalletBean
import com.wx.detalk.R
import com.wx.detalk.databinding.ActivityModifyPwBinding
import com.wx.detalk.mvvm.viewmodel.wallet.ModifyPwViewModel
import i18n

/**
 * Created by huy  on 2023/6/26.
 */
@Route(path = ARouterMap.MODIFY_PW)
class ModifyPwActivity : VbBaseActivity<ModifyPwViewModel, ActivityModifyPwBinding>(), OnClickListener {
    var address = ""
    var localWalletBean: LocalWalletBean? = null

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.ModifyPassword)
        address = intent.getStringExtra(ARouterMap.MODIFY_PW_ADDRESS).toString()

    }

    override fun initData() {
        localWalletBean = mViewModel.findWalletByAddress(address)
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.tvConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.tv_confirm -> {
                val passcode = localWalletBean!!.passCode
                val newPassCode = binding.etNewPw.text.toString()
                val repeatPasscode = binding.etRepeatPw.text.toString()
                if (newPassCode.isNotEmpty() && newPassCode == repeatPasscode) {
                    if (passcode == EncryptUtils.encryptMD5ToString(binding.etOldPw.text.toString())) {
                        mViewModel.updateWallet(localWalletBean!!, EncryptUtils.encryptMD5ToString(newPassCode))
                        finish()
                    } else {
                        ToastUtil.showToast("old password is wrong")
                    }
                } else {
                    ToastUtil.showToast("The two passwords do not match")
                    return
                }
            }
        }
    }

    override fun providerVMClass(): Class<ModifyPwViewModel> {
        return ModifyPwViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityModifyPwBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }
}