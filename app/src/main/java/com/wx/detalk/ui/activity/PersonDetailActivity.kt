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
import com.wx.detalk.R
import com.wx.detalk.adapter.CommunityPagerAdapter
import com.wx.detalk.databinding.ActivityPersonDetailBinding
import com.wx.detalk.mvvm.viewmodel.PersonDetailViewModel
import com.wx.detalk.ui.fragment.MinePostFragment
import i18n

/**
 * Created by huy  on 2023/6/27.
 */
@Route(path = ARouterMap.PERSON_DETAIL)
class PersonDetailActivity : VbBaseActivity<PersonDetailViewModel, ActivityPersonDetailBinding>(), OnClickListener {
    var tabList: MutableList<String> = ArrayList()
    var fragmentList: MutableList<Fragment> = ArrayList()

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        tabList.add(i18n(com.wx.base.R.string.Post))
        val bundle = Bundle()
        val minePostFragment = MinePostFragment()
        minePostFragment.arguments = bundle
        fragmentList.add(minePostFragment)
        binding.vpPersonDetail.adapter = CommunityPagerAdapter(this, fragmentList, tabList)
        binding.stlPersonDetail.setViewPager(binding.vpPersonDetail)
        binding.stlPersonDetail.currentTab = 0
        binding.vpPersonDetail.offscreenPageLimit = 2
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.tvRelation.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.tv_relation -> {
            }

        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityPersonDetailBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }
}