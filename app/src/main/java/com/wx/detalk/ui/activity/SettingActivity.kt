package com.wx.detalk.ui.activity

import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.SettingBean
import com.wx.detalk.R
import com.wx.detalk.adapter.SettingAdapter
import com.wx.detalk.databinding.ActivitySettingBinding
import com.wx.detalk.mvvm.viewmodel.mine.SettingViewModel
import i18n

/**
 * Created by huy  on 2023/6/20.
 */
@Route(path = ARouterMap.SETTING)
class SettingActivity : VbBaseActivity<SettingViewModel, ActivitySettingBinding>(), OnClickListener {
    var settingAdapter: SettingAdapter? = null
    var settingList: MutableList<SettingBean> = ArrayList()

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.Settings)

        settingList.add(SettingBean(com.wx.common.R.mipmap.ic_privacy, i18n(com.wx.base.R.string.Privacy)))
        settingList.add(SettingBean(com.wx.common.R.mipmap.ic_white_paper, i18n(com.wx.base.R.string.WhitePaper)))
        binding.rvSetting.setHasFixedSize(true)
        binding.rvSetting.layoutManager = LinearLayoutManager(BaseApplication.instance())
        settingAdapter = SettingAdapter(settingList, this)
        binding.rvSetting.adapter = settingAdapter
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.ll_setting -> {

            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }
}