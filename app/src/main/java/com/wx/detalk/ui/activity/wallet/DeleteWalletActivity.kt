package com.wx.detalk.ui.activity.wallet

import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.LocalWalletBean
import com.wx.common.event.LogOutEvent
import com.wx.common.realm.RealmUtil
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.databinding.ActivityDeleteWalletBinding
import com.wx.detalk.mvvm.viewmodel.wallet.DeleteWalletViewModel
import i18n
import org.greenrobot.eventbus.EventBus

/**
 * Created by huy  on 2023/6/26.
 */
@Route(path = ARouterMap.DELETE_WALLET)
class DeleteWalletActivity : VbBaseActivity<DeleteWalletViewModel, ActivityDeleteWalletBinding>(), OnClickListener {
    var wallet: LocalWalletBean? = null
    var address = ""
    var reason1Checked = false
    var reason2Checked = false
    var reason3Checked = false

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.DeleteWallet)

        address = intent.getStringExtra(ARouterMap.WALLET_ADDRESS).toString()
    }

    override fun initData() {
        val walletList = RealmUtil.instance?.findWalletByAddress(address)
        if (walletList.isNullOrEmpty()) {
        } else {
            wallet = walletList[0]
        }
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.tvConfirm.setOnClickListener(this)

        binding.cbReason1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                reason1Checked = true
                binding.tvConfirm.alpha = 1f
            } else {
                reason1Checked = false
                if (!reason2Checked && !reason3Checked) {
                    binding.tvConfirm.alpha = 0.4f
                }
            }
        }
        binding.cbReason2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                reason2Checked = true
                binding.tvConfirm.alpha = 1f
            } else {
                reason2Checked = false
                if (!reason1Checked && !reason3Checked) {
                    binding.tvConfirm.alpha = 0.4f
                }
            }
        }
        binding.cbReason3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                reason3Checked = true
                binding.tvConfirm.alpha = 1f
            } else {
                reason3Checked = false
                if (!reason1Checked && !reason2Checked) {
                    binding.tvConfirm.alpha = 0.4f
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.tv_confirm -> {
                if (binding.cbReason1.isChecked || binding.cbReason2.isChecked || binding.cbReason3.isChecked) {
                    val currentWallet = mViewModel.getCurrentWallet()
                    if (currentWallet!!.address == wallet!!.address) {
                        Web3jUtil.instance?.clearAddress()
                        EventBus.getDefault().post(LogOutEvent())
                    }
                    mViewModel.deleteWallet(wallet!!)
                    finish()
                }
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityDeleteWalletBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<DeleteWalletViewModel>? {
        return DeleteWalletViewModel::class.java
    }
}