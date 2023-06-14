package com.wx.detalk.startup

import com.wx.detalk.BuildConfig
import com.wx.base.proxy.OnAppBaseProxyLinsener


class OnAppBaseProxyManager : OnAppBaseProxyLinsener {
    override fun onIsProxy(): Boolean {
        return BuildConfig.is_proxy
    }

    override fun onIsDebug(): Boolean {
        return BuildConfig.is_debug
    }

}