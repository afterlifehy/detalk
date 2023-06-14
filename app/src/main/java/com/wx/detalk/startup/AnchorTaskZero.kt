package com.wx.detalk.startup

import com.wx.detalk.AppApplication
import com.wx.base.start.BaseStartUpManager
import com.wx.base.start.StartUpKey
import com.xj.anchortask.library.AnchorTask

class AnchorTaskZero : AnchorTask(StartUpKey.MUST_BE_INITIALIZED) {
    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        BaseStartUpManager.instance().applicationInit(AppApplication.instance())
        AppStartUpManager.instance().applicationInit(AppApplication.instance())
    }
}