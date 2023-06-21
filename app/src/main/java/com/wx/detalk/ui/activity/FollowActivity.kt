package com.wx.detalk.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.util.Constant
import com.wx.base.viewbase.VbBaseActivity
import com.wx.detalk.adapter.FollowPagerAdapter
import com.wx.detalk.databinding.ActivityFollowBinding
import com.wx.detalk.mvvm.viewmodel.mine.FollowAcViewModel
import com.wx.detalk.ui.fragment.FollowFragment
import i18n

/**
 * Created by huy  on 2023/6/21.
 */
@Route(path = ARouterMap.FOLLOW)
class FollowActivity : VbBaseActivity<FollowAcViewModel, ActivityFollowBinding>() {
    var tabList: MutableList<String> = ArrayList()
    var fragmentList: MutableList<Fragment> = ArrayList()

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        var followType = intent.getIntExtra("followType", Constant.FRIENDS)

        tabList.add(i18n(com.wx.base.R.string.Friends))
        tabList.add(i18n(com.wx.base.R.string.Following))
        tabList.add(i18n(com.wx.base.R.string.Followers))

        for (i in tabList.indices) {
            val bundle = Bundle()
            val followFragment = FollowFragment()
            bundle.putInt("followType", i)
            followFragment.arguments = bundle
            fragmentList.add(followFragment)
        }
        binding.vpFollow.adapter = FollowPagerAdapter(this@FollowActivity, fragmentList, tabList)
        binding.stlFollow.setViewPager(binding.vpFollow)
        binding.stlFollow.currentTab = followType
        binding.vpFollow.offscreenPageLimit = 2
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityFollowBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.ablToolbar
    }
}