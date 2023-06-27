package com.wx.detalk.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.viewbase.VbBaseActivity
import com.wx.detalk.adapter.CommunityPagerAdapter
import com.wx.detalk.databinding.ActivityMineDetailBinding
import com.wx.detalk.mvvm.viewmodel.mine.MineDetailViewModel
import com.wx.detalk.ui.fragment.DaoClubFragment
import com.wx.detalk.ui.fragment.MinePostFragment
import i18n

/**
 * Created by huy  on 2023/6/26.
 */
@Route(path = ARouterMap.MINE_DETAIL)
class MineDetailActivity : VbBaseActivity<MineDetailViewModel, ActivityMineDetailBinding>(), OnClickListener {
    var tabList: MutableList<String> = ArrayList()
    var fragmentList: MutableList<Fragment> = ArrayList()

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        tabList.add(i18n(com.wx.base.R.string.Post))
        tabList.add(i18n(com.wx.base.R.string.Reply))
        tabList.add(i18n(com.wx.base.R.string.Like))
        tabList.add(i18n(com.wx.base.R.string.Collect))

        for (i in tabList) {
            val bundle = Bundle()
            val minePostFragment = MinePostFragment()
            minePostFragment.arguments = bundle
            fragmentList.add(minePostFragment)
        }
        binding.vpMineDetail.adapter = CommunityPagerAdapter(this, fragmentList, tabList)
        binding.stlMineDetail.setViewPager(binding.vpMineDetail)
        binding.stlMineDetail.currentTab = 0
        binding.vpMineDetail.offscreenPageLimit = 2
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun onClick(v: View?) {
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityMineDetailBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<MineDetailViewModel>? {
        return MineDetailViewModel::class.java
    }
}