package com.wx.detalk.ui.fragment

import android.content.Intent
import android.util.Base64
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.base.mvvm.base.UrlManager
import com.wx.base.util.Constant
import com.wx.base.viewbase.VbBaseFragment
import com.wx.common.bean.LocalWalletBean
import com.wx.common.event.AddWalletSuccessEvent
import com.wx.common.event.LogOutEvent
import com.wx.common.util.GlideUtils
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.wallet.MineDaoClubAdapter
import com.wx.detalk.databinding.FragmentMineBinding
import com.wx.detalk.mvvm.viewmodel.mine.MineViewModel
import com.wx.detalk.ui.dialog.CreateWalletDialog
import com.wx.detalk.ui.dialog.WalletDialog
import i18n
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wallet.core.jni.AES
import wallet.core.jni.AESPaddingMode

/**
 * Created by huy  on 2023/6/14.
 */
class MineFragment : VbBaseFragment<MineViewModel, FragmentMineBinding>(), OnClickListener {
    var mineDaoList: MutableList<Int> = ArrayList()
    var mineDaoAdapter: MineDaoClubAdapter? = null
    var mineClubList: MutableList<Int> = ArrayList()
    var mineClubAdapter: MineDaoClubAdapter? = null

    var createWalletDialog: CreateWalletDialog? = null
    var walletDialog: WalletDialog? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(addWalletSuccessEvent: AddWalletSuccessEvent) {
        initWallet()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(logOutEvent: LogOutEvent) {
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivWallet, com.wx.common.R.mipmap.ic_wallet)
        binding.layoutToolbar.tvName.text = i18n(com.wx.base.R.string.Wallet)
        binding.tvAddress.gone()
    }

    override fun initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(binding.layoutToolbar.ablToolbar)

        binding.layoutDao.tvMineDaoClubTitle.text = i18n(com.wx.base.R.string.DAO)
        binding.layoutClub.tvMineDaoClubTitle.text = i18n(com.wx.base.R.string.CLUB)

        binding.layoutDao.rvMineDaoClub.isNestedScrollingEnabled = false
        binding.layoutDao.rvMineDaoClub.setHasFixedSize(true)
        binding.layoutDao.rvMineDaoClub.layoutManager = LinearLayoutManager(BaseApplication.instance())
        mineDaoAdapter = MineDaoClubAdapter(mineDaoList, this)
        binding.layoutDao.rvMineDaoClub.adapter = mineDaoAdapter

        binding.layoutClub.rvMineDaoClub.isNestedScrollingEnabled = false
        binding.layoutClub.rvMineDaoClub.setHasFixedSize(true)
        binding.layoutClub.rvMineDaoClub.layoutManager = LinearLayoutManager(BaseApplication.instance())
        mineClubAdapter = MineDaoClubAdapter(mineClubList, this)
        binding.layoutClub.rvMineDaoClub.adapter = mineClubAdapter
    }

    override fun initListener() {
        binding.layoutToolbar.rllWallet.setOnClickListener(this)
        binding.layoutToolbar.ivShare.setOnClickListener(this)
        binding.layoutToolbar.ivSetting.setOnClickListener(this)
        binding.llFriends.setOnClickListener(this)
        binding.llFollowing.setOnClickListener(this)
        binding.llFollower.setOnClickListener(this)
        binding.rlPerson.setOnClickListener(this)
        binding.tvAddress.setOnClickListener(this)
        binding.tvPost.setOnClickListener(this)
        binding.tvReply.setOnClickListener(this)
        binding.tvLike.setOnClickListener(this)
        binding.tvCollect.setOnClickListener(this)
        binding.layoutDao.tvCreateNow.setOnClickListener(this)
        binding.layoutClub.tvCreateNow.setOnClickListener(this)
    }

    override fun initData() {
        if (Web3jUtil.instance?.address != "") {
            initWallet()
        }

        binding.layoutDao.tvCreateNow.gone()
        binding.layoutDao.rvMineDaoClub.show()
        mineDaoList.add(1)
        mineDaoList.add(1)
        mineDaoList.add(1)
        mineDaoList.add(1)
        mineDaoList.add(1)
        mineDaoAdapter?.setList(mineDaoList)

        binding.layoutClub.tvCreateNow.gone()
        binding.layoutClub.rvMineDaoClub.show()
        mineClubList.add(1)
        mineClubList.add(1)
        mineClubList.add(1)
        mineClubList.add(1)
        mineClubList.add(1)
        mineClubAdapter?.setList(mineClubList)
    }

    override fun onClick(v: View?) {
        val walletList = mViewModel.findWalletList()
        if (Web3jUtil.instance?.address.isNullOrEmpty() && v?.id != R.id.iv_setting) {
            if (walletList.isNullOrEmpty()) {
                if (createWalletDialog == null) {
                    createWalletDialog = CreateWalletDialog(object : CreateWalletDialog.CreateWalletCallBack {
                        override fun createWallet() {
                            ARouter.getInstance().build(ARouterMap.SELECT_NETWORK)
                                .withInt(ARouterMap.ADD_WALLET_TYPE, Constant.CREATE_WALLET)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                        }

                        override fun importWallet() {
                            ARouter.getInstance().build(ARouterMap.SELECT_NETWORK)
                                .withInt(ARouterMap.ADD_WALLET_TYPE, Constant.IMPORT_WALLET)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                        }

                    })
                }
                createWalletDialog?.show()
                return
            } else {
                walletDialog = WalletDialog(object : WalletDialog.SwitchWalletCallback {
                    override fun switchWallet(localWalletBean: LocalWalletBean) {
                        val mnemonicAes = localWalletBean.mnemonicAes
                        val code = localWalletBean.passCode
                        val mnemonic = AES.decryptCBC(
                            code.toByteArray(),
                            Base64.decode(mnemonicAes, Base64.DEFAULT),
                            code.toByteArray(),
                            AESPaddingMode.PKCS7
                        )
                        runBlocking {
                            PreferencesDataStore(BaseApplication.instance()).putLong(
                                PreferencesKeys.chainId,
                                localWalletBean.chainId
                            )
                        }
                        Web3jUtil.instance?.setChainId(localWalletBean.chainId)
                        Web3jUtil.instance?.importWallet(String(mnemonic), "")
                        Web3jUtil.instance?.buildWeb3j(UrlManager.getWeb3jHttpUrl())
                        Web3jUtil.instance?.getWalletAddress()
                        mViewModel.updateWallet(localWalletBean, System.currentTimeMillis())
                        EventBus.getDefault().post(AddWalletSuccessEvent())
                    }

                })
                walletDialog?.show()
            }
        }
        when (v?.id) {
            R.id.rll_wallet -> {
                walletDialog = WalletDialog(object : WalletDialog.SwitchWalletCallback {
                    override fun switchWallet(localWalletBean: LocalWalletBean) {
                        val mnemonicAes = localWalletBean.mnemonicAes
                        val code = localWalletBean.passCode
                        val mnemonic = AES.decryptCBC(
                            code.toByteArray(),
                            Base64.decode(mnemonicAes, Base64.DEFAULT),
                            code.toByteArray(),
                            AESPaddingMode.PKCS7
                        )
                        runBlocking {
                            PreferencesDataStore(BaseApplication.instance()).putLong(
                                PreferencesKeys.chainId,
                                localWalletBean.chainId
                            )
                        }
                        Web3jUtil.instance?.setChainId(localWalletBean.chainId)
                        Web3jUtil.instance?.importWallet(String(mnemonic), "")
                        Web3jUtil.instance?.buildWeb3j(UrlManager.getWeb3jHttpUrl())
                        Web3jUtil.instance?.getWalletAddress()
                        mViewModel.updateWallet(localWalletBean, System.currentTimeMillis())
                        EventBus.getDefault().post(AddWalletSuccessEvent())
                    }

                })
                walletDialog?.show()
            }

            R.id.iv_share -> {
                ARouter.getInstance().build(ARouterMap.SHARE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            }

            R.id.iv_setting -> {
                ARouter.getInstance().build(ARouterMap.SETTING).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            }

            R.id.rl_person -> {

            }

            R.id.tv_address -> {
                ClipboardUtils.copyText(binding.tvAddress.text.toString())
            }

            R.id.ll_friends, R.id.ll_following, R.id.ll_follower -> {
                var followType = when (v.id) {
                    R.id.ll_friends -> {
                        Constant.FRIENDS
                    }

                    R.id.ll_following -> {
                        Constant.FOLLOWING
                    }

                    R.id.ll_follower -> {
                        Constant.FOLLOWER
                    }

                    else -> {
                        Constant.FRIENDS
                    }
                }
                ARouter.getInstance().build(ARouterMap.FOLLOW).withInt("followType", followType)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            }

            R.id.tv_post, R.id.tv_reply, R.id.tv_like, R.id.tv_collect -> {
                ARouter.getInstance().build(ARouterMap.MINE_DETAIL)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            }

            R.id.tv_createNow -> {
                if (v == binding.layoutDao.tvCreateNow) {

                } else if (v == binding.layoutClub.tvCreateNow) {

                }
            }
        }
    }

    fun initWallet() {
        val localWalletBean = mViewModel.getCurrentWallet()
        if (localWalletBean!!.chainId == Web3jUtil.instance?.BSC_CHAIN_ID_DEV || localWalletBean.chainId == Web3jUtil.instance?.BSC_CHAIN_ID_RELEASE) {
            GlideUtils.instance?.loadImage(binding.layoutToolbar.ivWallet, com.wx.common.R.mipmap.ic_bsc)
        } else {
            GlideUtils.instance?.loadImage(binding.layoutToolbar.ivWallet, com.wx.common.R.mipmap.ic_eth)
        }
        binding.layoutToolbar.tvName.text = i18n(com.wx.base.R.string.Detalk)
        binding.tvAddress.show()
        binding.tvAddress.text = localWalletBean.address
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

        }
    }

    override fun providerVMClass(): Class<MineViewModel> {
        return MineViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentMineBinding.inflate(layoutInflater)
    }

    override fun isRegEventBus(): Boolean {
        return true
    }

}