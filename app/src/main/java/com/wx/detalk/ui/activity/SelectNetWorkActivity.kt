package com.wx.detalk.ui.activity

import android.content.Intent
import android.util.ArrayMap
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.wx.base.BaseApplication
import com.wx.base.BuildConfig
import com.wx.base.arouter.ARouterMap
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.util.Constant
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.NetworkBean
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.NetworkAdapter
import com.wx.detalk.databinding.ActivitySelectNetworkBinding
import com.wx.detalk.mvvm.viewmodel.home.SelectNetworkViewModel
import i18n
import kotlinx.coroutines.runBlocking

/**
 * Created by huy  on 2023/6/15.
 */
@Route(path = ARouterMap.SELECT_NETWORK)
class SelectNetWorkActivity : VbBaseActivity<SelectNetworkViewModel, ActivitySelectNetworkBinding>(), OnClickListener {
    var networkAdapter: NetworkAdapter? = null
    var networkList: MutableList<NetworkBean>? = ArrayList()
    var importWalletType = Constant.CREATE_WALLET
    val bscChainIdMap: MutableMap<Boolean, Long> = ArrayMap()
    val ethChainIdMap: MutableMap<Boolean, Long> = ArrayMap()

    init {
        bscChainIdMap[true] = 97L
        bscChainIdMap[false] = 56L

        ethChainIdMap[true] = 5L
        ethChainIdMap[false] = 1L
    }

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.seletc_a_network)
        importWalletType = intent.getIntExtra(ARouterMap.ADD_WALLET_TYPE, Constant.CREATE_WALLET)

        binding.rvNetwork.setHasFixedSize(true)
        binding.rvNetwork.layoutManager = LinearLayoutManager(BaseApplication.instance())
        networkAdapter = NetworkAdapter(networkList, this)
        binding.rvNetwork.adapter = networkAdapter
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
    }

    override fun initData() {
        networkList?.add(NetworkBean(com.wx.common.R.mipmap.ic_eth, "Ethereum", ethChainIdMap[BuildConfig.is_dev]!!))
        networkList?.add(NetworkBean(com.wx.common.R.mipmap.ic_bnb, "BNB Chain", bscChainIdMap[BuildConfig.is_dev]!!))

        networkAdapter?.setList(networkList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.ll_network -> {
                runBlocking {
                    val network = v.tag as NetworkBean
                    PreferencesDataStore(BaseApplication.instance()).putLong(PreferencesKeys.chainId, network.chainId)
                    Web3jUtil.instance?.setChainId(network.chainId)
                    when (importWalletType) {
                        Constant.CREATE_WALLET -> {
                            ARouter.getInstance().build(ARouterMap.SECURITY)
                                .withInt(ARouterMap.SECURITY_TYPE, Constant.SECURITY_CREATE)
                                .withString(ARouterMap.SECURITY_DESTINATION, ARouterMap.SEED_PHRASE)
                                .withString(ARouterMap.SECURITY_PARAM_KEY, ARouterMap.SEED_PHRASE_TYPE)
                                .withInt(ARouterMap.SECURITY_PARAM_VALUE, Constant.SEED_PHRASE_SHOW)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation()
                        }

                        Constant.IMPORT_WALLET -> {
                            ARouter.getInstance().build(ARouterMap.SECURITY)
                                .withInt(ARouterMap.SECURITY_TYPE, Constant.SECURITY_CREATE)
                                .withString(ARouterMap.SECURITY_DESTINATION, ARouterMap.IMPORT_MNEMONIC)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation()
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivitySelectNetworkBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

}