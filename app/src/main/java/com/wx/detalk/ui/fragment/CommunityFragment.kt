package com.wx.detalk.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.wx.base.BaseApplication
import com.wx.base.ext.gone
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.adapter.BannerAdAdapter
import com.wx.detalk.adapter.CommunityPagerAdapter
import com.wx.detalk.databinding.FragmentCommunityBinding
import com.wx.detalk.mvvm.viewmodel.community.CommunityViewModel
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.listener.OnPageChangeListener
import i18n

/**
 * Created by huy  on 2023/6/14.
 */
class CommunityFragment : VbBaseFragment<CommunityViewModel, FragmentCommunityBinding>(), OnClickListener {
    var adList: MutableList<String> = ArrayList()
    var tabList: MutableList<String> = ArrayList()
    var fragmentList: MutableList<Fragment> = ArrayList()

    override fun initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(binding.layoutToolbar.ablToolbar)
        binding.layoutToolbar.ivBack.gone()
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.Community)

        adList.add("https://cdn-usa.skypixel.com/uploads/usa_files/storynode/image/4c57df32-a28a-49ea-93b8-2fa35341390a.jpg@!1200")
        adList.add("https://cdn-usa.skypixel.com/uploads/usa_files/storynode/image/4c57df32-a28a-49ea-93b8-2fa35341390a.jpg@!1200")
        adList.add("https://cdn-usa.skypixel.com/uploads/usa_files/storynode/image/1a3a6030-099d-4c2b-b85d-a4da19d41e6b.jpg@!1200")
        adList.add("https://cdn-hz.skypixel.com/uploads/cn_files/storynode/image/80bc4daa-db21-4ee1-af6f-d92834f37015.jpg@!1200")
        adList.add("https://cdn-hz.skypixel.com/uploads/cn_files/storynode/image/ed0d911c-5043-49b8-a479-ce63fbc67bdd.jpg@!1200")
        binding.baAd.addBannerLifecycleObserver(this)
            .setAdapter(BannerAdAdapter(adList))
            .indicator = RoundLinesIndicator(BaseApplication.instance())
        binding.baAd.setOnBannerListener(object : OnBannerListener<Any> {
            override fun OnBannerClick(data: Any?, position: Int) {

            }
        })

        tabList.add(i18n(com.wx.base.R.string.DAO))
        tabList.add(i18n(com.wx.base.R.string.CLUB))

        for (i in tabList) {
            val bundle = Bundle()
            val daoClubFragment = DaoClubFragment()
            daoClubFragment.arguments = bundle
            fragmentList.add(daoClubFragment)
        }
        binding.vpCommunity.adapter = CommunityPagerAdapter(requireActivity(), fragmentList, tabList)
        binding.stlCommunity.setViewPager(binding.vpCommunity)
        binding.stlCommunity.currentTab = 0
        binding.vpCommunity.offscreenPageLimit = 2
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentCommunityBinding.inflate(layoutInflater)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

}