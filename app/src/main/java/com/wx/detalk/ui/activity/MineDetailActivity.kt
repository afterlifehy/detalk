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
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.CommunityPagerAdapter
import com.wx.detalk.databinding.ActivityMineDetailBinding
import com.wx.detalk.mvvm.viewmodel.mine.MineDetailViewModel
import com.wx.detalk.ui.dialog.MineEditDialog
import com.wx.detalk.ui.fragment.DaoClubFragment
import com.wx.detalk.ui.fragment.MinePostFragment
import com.wx.detalk.ui.fragment.MineReplyFragment
import i18n

/**
 * Created by huy  on 2023/6/26.
 */
@Route(path = ARouterMap.MINE_DETAIL)
class MineDetailActivity : VbBaseActivity<MineDetailViewModel, ActivityMineDetailBinding>(), OnClickListener {
    var tabList: MutableList<String> = ArrayList()
    var fragmentList: MutableList<Fragment> = ArrayList()
    var mineEditDialog: MineEditDialog? = null

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        val tab = intent.getIntExtra(ARouterMap.MINE_DETAIL_TAB, 0)
        binding.tvAddress.text = Web3jUtil.instance?.address

        tabList.add(i18n(com.wx.base.R.string.Post))
        tabList.add(i18n(com.wx.base.R.string.Reply))
        tabList.add(i18n(com.wx.base.R.string.Like))
        tabList.add(i18n(com.wx.base.R.string.Collect))

        for (i in tabList.indices) {
            if (i == 1) {
                val bundle = Bundle()
                val mineReplyFragment = MineReplyFragment()
                mineReplyFragment.arguments = bundle
                fragmentList.add(mineReplyFragment)
            } else {
                val bundle = Bundle()
                val minePostFragment = MinePostFragment()
                minePostFragment.arguments = bundle
                fragmentList.add(minePostFragment)
            }
        }
        binding.vpMineDetail.adapter = CommunityPagerAdapter(this, fragmentList, tabList)
        binding.stlMineDetail.setViewPager(binding.vpMineDetail)
        binding.stlMineDetail.currentTab = tab
        binding.vpMineDetail.offscreenPageLimit = 2
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.rtvEdit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.rtv_edit -> {
                mineEditDialog = null
                mineEditDialog = MineEditDialog(object : MineEditDialog.MineEditCallBack {
                    override fun save(name: String, avatar: String) {
                        binding.tvName.text = name
                    }

                })
                mineEditDialog?.show()
            }

        }
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