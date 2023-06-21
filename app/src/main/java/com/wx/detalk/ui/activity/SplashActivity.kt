package com.wx.detalk.ui.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.start.StartUpKey
import com.wx.base.viewbase.VbBaseActivity
import com.wx.detalk.databinding.ActivitySplashBinding
import com.wx.detalk.mvvm.viewmodel.SplashViewModel
import com.wx.detalk.startup.ApplicationAnchorTaskCreator
import com.xj.anchortask.library.AnchorProject
import com.xj.anchortask.library.OnProjectExecuteListener
import com.xj.anchortask.library.log.LogUtils
import kotlinx.coroutines.runBlocking

/**
 * Created by huy  on 2023/6/14.
 */
class SplashActivity : VbBaseActivity<SplashViewModel, ActivitySplashBinding>(), OnProjectExecuteListener {
    var project: AnchorProject? = null

    override fun onResume() {
        super.onResume()
        project?.start()
    }

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        setCustomDensity()

        project = AnchorProject.Builder().setContext(this).setLogLevel(LogUtils.LogLevel.DEBUG)
            .setAnchorTaskCreator(ApplicationAnchorTaskCreator())
            .addTask(StartUpKey.MUST_BE_INITIALIZED)
            .addTask(StartUpKey.MUST_BE_ONE).afterTask(StartUpKey.MUST_BE_INITIALIZED)
            .build()
        project?.addListener(this)
    }

    fun setCustomDensity() {
        val appDisplayMetrics: DisplayMetrics = application.resources.displayMetrics

        val targetDensity = appDisplayMetrics.heightPixels / 812f
        val targetDensityDpi = (160 * targetDensity).toInt()
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        val activityDisplayMetrics = resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun onProjectFinish() {
        Handler(Looper.getMainLooper()).postDelayed({
            runBlocking {
                val isAppHaveLaunched =
                    PreferencesDataStore(BaseApplication.instance()).getBoolean(PreferencesKeys.isAppHaveLaunched)
                if(isAppHaveLaunched){
                    ARouter.getInstance().build(ARouterMap.MAIN).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                }else{
                    ARouter.getInstance().build(ARouterMap.GUIDE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                }
                finish()
            }
        }, 1000)
    }

    override fun onProjectStart() {
    }

    override fun onTaskFinish(taskName: String) {
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

}