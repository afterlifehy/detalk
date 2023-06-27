package com.wx.detalk.ui.dialog

import android.content.DialogInterface
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.wx.base.BaseApplication
import com.wx.base.BuildConfig
import com.wx.base.arouter.ARouterMap
import com.wx.base.dialog.VBBaseLibDialog
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.base.help.ActivityCacheManager
import com.wx.base.util.Constant
import com.wx.common.bean.LocalWalletBean
import com.wx.common.event.AddWalletSuccessEvent
import com.wx.common.realm.RealmUtil
import com.wx.common.util.AppFlag
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.WalletAdapter
import com.wx.detalk.databinding.DialogWalletBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by huy  on 2023/6/21.
 */
class WalletDialog(val callback: SwitchWalletCallback) : VBBaseLibDialog<DialogWalletBinding>(
    ActivityCacheManager.instance().getCurrentActivity()!!,
    com.wx.base.R.style.CommonBottomDialogStyle
), OnClickListener {
    var walletList: MutableList<LocalWalletBean> = ArrayList()
    var ethWalletList: MutableList<LocalWalletBean> = ArrayList()
    var bscWalletList: MutableList<LocalWalletBean> = ArrayList()
    var walletAdapter: WalletAdapter? = null
    var currentWallet: LocalWalletBean? = null

    var createWalletDialog: CreateWalletDialog? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(addWalletSuccessEvent: AddWalletSuccessEvent) {
        dismiss()
    }

    init {
        initView()
    }

    private fun initView() {
        EventBus.getDefault().register(this)

        bscWalletList.addAll(RealmUtil.instance?.findBSCWalletList()!!)
        ethWalletList.addAll(RealmUtil.instance?.findETHWalletList()!!)
        val wallets = RealmUtil.instance?.findWalletByAddress(Web3jUtil.instance?.address.toString())
        if (wallets.isNullOrEmpty()) {
            currentWallet = null
        } else {
            currentWallet = wallets[0]
        }

        binding.rvWallet.setHasFixedSize(true)
        binding.rvWallet.layoutManager = LinearLayoutManager(BaseApplication.instance())
        walletAdapter = WalletAdapter(walletList, currentWallet, this)
        binding.rvWallet.adapter = walletAdapter

        binding.ivClose.setOnClickListener(this)
        binding.ivAddWallet.setOnClickListener(this)
        binding.rgChain.setOnCheckedChangeListener { group, checkedId ->
            walletList.clear()
            if (checkedId == binding.rbBnb.id) {
                walletList.addAll(bscWalletList)
            } else {
                walletList.addAll(ethWalletList)
            }
            if (walletList.isEmpty()) {
                binding.rvWallet.gone()
                binding.tvNoData.show()
            } else {
                binding.rvWallet.show()
                binding.tvNoData.gone()
                walletAdapter?.setList(walletList)
            }

        }

        if (currentWallet?.chainId == Web3jUtil.instance?.BSC_CHAIN_ID_DEV || currentWallet?.chainId == Web3jUtil.instance?.BSC_CHAIN_ID_RELEASE) {
            binding.rbBnb.isChecked = true
        } else {
            binding.rbEth.isChecked = true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }

            R.id.iv_addWallet -> {
                if (binding.rbBnb.isChecked) {
                    Constant.chainId = AppFlag.bscChainIdMap[BuildConfig.is_dev]!!
                } else {
                    Constant.chainId = AppFlag.ethChainIdMap[BuildConfig.is_dev]!!
                }
                if (createWalletDialog == null) {
                    createWalletDialog = CreateWalletDialog(object : CreateWalletDialog.CreateWalletCallBack {
                        override fun createWallet() {
                            ARouter.getInstance().build(ARouterMap.SECURITY)
                                .withInt(ARouterMap.SECURITY_TYPE, Constant.SECURITY_CREATE)
                                .withString(ARouterMap.SECURITY_DESTINATION, ARouterMap.SEED_PHRASE)
                                .withString(ARouterMap.SECURITY_PARAM_KEY, ARouterMap.SEED_PHRASE_TYPE)
                                .withInt(ARouterMap.SECURITY_PARAM_VALUE, Constant.SEED_PHRASE_SHOW)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation()
                        }

                        override fun importWallet() {
                            ARouter.getInstance().build(ARouterMap.SECURITY)
                                .withInt(ARouterMap.SECURITY_TYPE, Constant.SECURITY_CREATE)
                                .withString(ARouterMap.SECURITY_DESTINATION, ARouterMap.IMPORT_MNEMONIC)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation()
                        }

                    })
                }
                createWalletDialog?.show()
            }

            R.id.rfl_wallet, R.id.tv_name, R.id.tv_address -> {
                val wallet = v.tag as LocalWalletBean
                callback.switchWallet(wallet)
            }

            R.id.tv_copy -> {
                val wallet = v.tag as LocalWalletBean
                ClipboardUtils.copyText(wallet.address)
            }

            R.id.rrl_wallet -> {
                val wallet = v.tag as LocalWalletBean
                ARouter.getInstance().build(ARouterMap.WALLET_DETAIL)
                    .withString(ARouterMap.WALLET_ADDRESS, wallet.address).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogWalletBinding.inflate(layoutInflater)
    }

    override fun getHideInput(): Boolean {
        return true
    }

    override fun getWidth(): Float {
        return WindowManager.LayoutParams.MATCH_PARENT.toFloat()
    }

    override fun getHeight(): Float {
        return SizeUtils.dp2px(466f).toFloat()
    }

    override fun getCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun getGravity(): Int {
        return Gravity.BOTTOM
    }

    interface SwitchWalletCallback {
        fun switchWallet(wallet: LocalWalletBean)
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        EventBus.getDefault().unregister(this)
    }
}