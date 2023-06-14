package com.wx.detalk.ui.activity

import androidx.viewbinding.ViewBinding
import com.wx.base.viewbase.VbBaseActivity
import com.wx.detalk.databinding.ActivitySplashBinding
import com.wx.detalk.mvvm.viewmodel.SplashViewModel

/**
 * Created by huy  on 2023/6/14.
 */
class SplashActivity : VbBaseActivity<SplashViewModel, ActivitySplashBinding>() {
    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true


}