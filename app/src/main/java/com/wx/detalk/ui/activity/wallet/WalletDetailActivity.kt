package com.wx.detalk.ui.activity.wallet

import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.EncryptUtils
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.util.Constant
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.LocalWalletBean
import com.wx.common.bean.WalletDetailBean
import com.wx.common.event.LogOutEvent
import com.wx.common.realm.RealmUtil
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.WalletDetailAdapter
import com.wx.detalk.databinding.ActivityWalletDetailBinding
import com.wx.detalk.mvvm.viewmodel.wallet.WalletDetailViewModel
import com.wx.detalk.ui.dialog.ExportSeedPhraseDialog
import i18n
import org.greenrobot.eventbus.EventBus

/**
 * Created by huy  on 2023/6/20.
 */
@Route(path = ARouterMap.WALLET_DETAIL)
class WalletDetailActivity : VbBaseActivity<WalletDetailViewModel, ActivityWalletDetailBinding>(), OnClickListener {
    var walletDetailAdapter: WalletDetailAdapter? = null
    var walletDetailList: MutableList<WalletDetailBean> = ArrayList()
    var exportSeedPhraseDialog: ExportSeedPhraseDialog? = null
    var wallet: LocalWalletBean? = null
    var address = ""

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.WalletDetail)
        address = intent.getStringExtra(ARouterMap.WALLET_ADDRESS).toString()

        walletDetailList.add(
            WalletDetailBean(
                com.wx.common.R.mipmap.ic_export_seed_phrase,
                i18n(com.wx.base.R.string.ExportSeedPhrase)
            )
        )
        walletDetailList.add(
            WalletDetailBean(
                com.wx.common.R.mipmap.ic_modify_pw,
                i18n(com.wx.base.R.string.ModifyPassword)
            )
        )
        binding.rvWalletDetail.setHasFixedSize(true)
        binding.rvWalletDetail.layoutManager = LinearLayoutManager(BaseApplication.instance())
        walletDetailAdapter = WalletDetailAdapter(walletDetailList, this)
        binding.rvWalletDetail.adapter = walletDetailAdapter
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
        binding.rtvDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.ll_walletDetail -> {
                val walletDetailBean = v.tag as WalletDetailBean
                when (walletDetailBean.icon) {
                    com.wx.common.R.mipmap.ic_export_seed_phrase -> {
                        exportSeedPhraseDialog = null
                        exportSeedPhraseDialog =
                            ExportSeedPhraseDialog(object : ExportSeedPhraseDialog.ExportSeedPhraseCallBack {
                                override fun confirm(pw: String) {
                                    val passcode = wallet!!.passCode
                                    if (passcode == EncryptUtils.encryptMD5ToString(pw)) {
                                        ARouter.getInstance().build(ARouterMap.SEED_PHRASE)
                                            .withInt(ARouterMap.SEED_PHRASE_TYPE, Constant.SEED_PHRASE_BACKUP)
                                            .withString(ARouterMap.SEED_PHRASE_ADDRESS, address)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .navigation()
                                    } else {
                                        return
                                    }
                                }

                            })
                        exportSeedPhraseDialog?.show()
                    }

                    com.wx.common.R.mipmap.ic_modify_pw -> {
                        ARouter.getInstance().build(ARouterMap.MODIFY_PW)
                            .withString(ARouterMap.MODIFY_PW_ADDRESS, address).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .navigation()
                    }
                }
            }

            R.id.rtv_delete -> {
                ARouter.getInstance().build(ARouterMap.DELETE_WALLET)
                    .withString(ARouterMap.DELETE_WALLET_ADDRESS, address).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            }
        }
    }

    override fun providerVMClass(): Class<WalletDetailViewModel>? {
        return WalletDetailViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityWalletDetailBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }
}