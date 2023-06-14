package com.wx.detalk.startup

import android.app.Application
import com.wx.base.BaseApplication
import com.wx.base.start.AppInitManager
import com.wx.common.smart.SmartRefreshHelp

class AppStartUpManager private constructor() : AppInitManager() {
    private val TAG: String = AppStartUpManager::class.java.simpleName

    companion object {
        var mAppStartUpManager: AppStartUpManager? = null
        fun instance(): AppStartUpManager {
            if (mAppStartUpManager == null) {
                mAppStartUpManager = AppStartUpManager()
            }
            return mAppStartUpManager!!
        }
    }

    override fun applicationInit(application: Application) {
//        CrashHandler.instance()?.initCrash(application)
        BaseApplication.instance().setOnAppBaseProxyLinsener(OnAppBaseProxyManager())        //初始化全局的刷新
        SmartRefreshHelp.initRefHead()
        //初始化网络状态监听

    }

    override fun delayInit(application: Application) {

    }
}