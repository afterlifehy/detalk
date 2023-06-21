package com.wx.detalk.ui.activity

import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.GuideBean
import com.wx.detalk.R
import com.wx.detalk.adapter.BannerGuideAdapter
import com.wx.detalk.databinding.ActivityGuideBinding
import com.wx.detalk.mvvm.viewmodel.GuideViewModel
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.listener.OnPageChangeListener
import i18n
import kotlinx.coroutines.runBlocking

/**
 * Created by huy  on 2023/6/21.
 */
@Route(path = ARouterMap.GUIDE)
class GuideActivity : VbBaseActivity<GuideViewModel, ActivityGuideBinding>(), OnClickListener {
    var guideList: MutableList<GuideBean> = ArrayList()

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        guideList.add(
            GuideBean(
                com.wx.common.R.mipmap.ic_guide,
                i18n(com.wx.base.R.string.holding_token_to_talk),
                i18n(com.wx.base.R.string.posting_high_quality_information)
            )
        )
        guideList.add(
            GuideBean(
                com.wx.common.R.mipmap.ic_guide2,
                i18n(com.wx.base.R.string.digital_marketing_solution),
                i18n(com.wx.base.R.string.create_a_bridge_for_business_to_web3_0_world)
            )
        )
        guideList.add(
            GuideBean(
                com.wx.common.R.mipmap.ic_guide3,
                i18n(com.wx.base.R.string.open_platform),
                i18n(com.wx.base.R.string.to_bulid_decentralized_platform)
            )
        )
        binding.baGuide.addBannerLifecycleObserver(this)
            .setAdapter(BannerGuideAdapter(guideList))
            .indicator = RectangleIndicator(BaseApplication.instance())
        binding.baGuide.setOnBannerListener { data, position -> }
        binding.baGuide.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.baGuide.removeIndicator()
                    binding.ivGo.show()
                } else {
                    binding.baGuide.indicator = RectangleIndicator(BaseApplication.instance())
                    binding.ivGo.gone()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.tvSkip.setOnClickListener(this)
        binding.ivGo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_skip, R.id.iv_go -> {
                runBlocking {
                    PreferencesDataStore(BaseApplication.instance()).putBoolean(PreferencesKeys.isAppHaveLaunched, true)
                    ARouter.getInstance().build(ARouterMap.MAIN).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                    finish()
                }
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityGuideBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.tvSkip
    }
}