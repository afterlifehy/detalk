package com.wx.detalk.ui.activity

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.BottomTabBean
import com.wx.detalk.R
import com.wx.detalk.adapter.BottomTabAdapter
import com.wx.detalk.databinding.ActivityMainBinding
import com.wx.detalk.mvvm.viewmodel.MainViewModel
import com.wx.detalk.ui.fragment.CommunityFragment
import com.wx.detalk.ui.fragment.HomeFragment
import com.wx.detalk.ui.fragment.MineFragment
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

    override fun initView() {
        BarUtils.setNavBarVisibility(this@MainActivity, false)
        initBottomTab()
    }

    private fun initBottomTab() {
        homeBottomTab = BottomTabBean(
            "",
            HomeFragment(),
            com.wx.common.R.mipmap.ic_setting,
            com.wx.common.R.mipmap.ic_setting
        )
        bottomTabList.add(homeBottomTab!!)
        communityBottomTab = BottomTabBean(
            "",
            CommunityFragment(),
            com.wx.common.R.mipmap.ic_setting,
            com.wx.common.R.mipmap.ic_setting
        )
        bottomTabList.add(communityBottomTab!!)
        mineBottomTab = BottomTabBean(
            "",
            MineFragment(),
            com.wx.common.R.mipmap.ic_setting,
            com.wx.common.R.mipmap.ic_setting
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
            R.id.fl_mainContainer,
            item?.tabName.toString()
        )
    }

    override fun initData() {
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true


}