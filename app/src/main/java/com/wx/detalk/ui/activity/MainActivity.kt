package com.wx.detalk.ui.activity

import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.ui.util.StatusBarUtil
import com.blankj.utilcode.util.BarUtils
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.mvvm.base.UrlManager
import com.wx.base.util.ToastUtil
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.BottomTabBean
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.BottomTabAdapter
import com.wx.detalk.databinding.ActivityMainBinding
import com.wx.detalk.mvvm.viewmodel.MainViewModel
import com.wx.detalk.ui.fragment.CommunityFragment
import com.wx.detalk.ui.fragment.HomeFragment
import com.wx.detalk.ui.fragment.MineFragment
import kotlinx.coroutines.runBlocking
import wallet.core.jni.AES
import wallet.core.jni.AESPaddingMode
import wallet.core.jni.CoinType
import java.util.ArrayList

/**
 * Created by huy  on 2023/6/14.
 */
@Route(path = ARouterMap.MAIN)
class MainActivity : VbBaseActivity<MainViewModel, ActivityMainBinding>(), View.OnClickListener,
    BottomTabAdapter.OnTabSelectLinsener {
    private val bottomTabList = ArrayList<BottomTabBean>()
    private var mBottomTabAdapter: BottomTabAdapter? = null
    private var homeBottomTab: BottomTabBean? = null
    private var communityBottomTab: BottomTabBean? = null
    private var mineBottomTab: BottomTabBean? = null

    override fun onSaveInstanceState(outState: Bundle) {
        // super.onSaveInstanceState(outState)
    }

    override fun initView() {
        BarUtils.setNavBarVisibility(this@MainActivity, false)
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        initBottomTab()
    }

    private fun initBottomTab() {
        homeBottomTab = BottomTabBean(
            "home",
            HomeFragment(),
            com.wx.common.R.mipmap.ic_home_checked,
            com.wx.common.R.mipmap.ic_home_unchecked
        )
        bottomTabList.add(homeBottomTab!!)

        communityBottomTab = BottomTabBean(
            "community",
            CommunityFragment(),
            com.wx.common.R.mipmap.ic_community_checked,
            com.wx.common.R.mipmap.ic_community_unchecked
        )
        bottomTabList.add(communityBottomTab!!)

        mineBottomTab = BottomTabBean(
            "mine",
            MineFragment(),
            com.wx.common.R.mipmap.ic_mine_checked,
            com.wx.common.R.mipmap.ic_mine_unchecked
        )
        bottomTabList.add(mineBottomTab!!)

        binding.rvBottomTab.setHasFixedSize(true)
        binding.rvBottomTab.layoutManager =
            StaggeredGridLayoutManager(bottomTabList.size, StaggeredGridLayoutManager.VERTICAL)
        mBottomTabAdapter = BottomTabAdapter(bottomTabList, this@MainActivity)
        binding.rvBottomTab.adapter = mBottomTabAdapter
        onTabSelectLinsener(0, homeBottomTab)
    }

    override fun initListener() {
    }

    override fun onClick(v: View?) {

    }

    override fun onTabSelectLinsener(tabIndex: Int, item: BottomTabBean?) {
        showFragment(
            supportFragmentManager,
            supportFragmentManager.beginTransaction(),
            item?.mFragment,
            R.id.fl_container,
            item?.tabName.toString()
        )
    }

    override fun initData() {
        runBlocking {
            val currentWallet = mViewModel.getCurrentWallet()
            if (currentWallet != null) {
                val mnemonicAes = currentWallet.mnemonicAes
                val code = currentWallet.passCode
                try {
                    val mnemonic = AES.decryptCBC(
                        code.toByteArray(),
                        Base64.decode(mnemonicAes, Base64.DEFAULT),
                        code.toByteArray(),
                        AESPaddingMode.PKCS7
                    )
                    PreferencesDataStore(BaseApplication.instance()).putLong(
                        PreferencesKeys.chainId,
                        currentWallet.chainId
                    )
                    Web3jUtil.instance?.setChainId(currentWallet.chainId)
                    Web3jUtil.instance?.importWallet(String(mnemonic), "")
                    Web3jUtil.instance?.buildWeb3j(UrlManager.getWeb3jHttpUrl())
                    Web3jUtil.instance?.getWalletAddress()
                } catch (e: Exception) {
                    ToastUtil.showToast(e.toString())
                }
            }
        }
    }

    override fun providerVMClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun marginStatusBarView(): View? {
        return super.marginStatusBarView()
    }

    override val isFullScreen: Boolean
        get() = true

}