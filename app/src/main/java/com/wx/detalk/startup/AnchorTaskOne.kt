package com.wx.detalk.startup

import com.wx.detalk.AppApplication
import com.wx.base.start.BaseStartUpManager
import com.wx.base.start.StartUpKey
import com.xj.anchortask.library.AnchorTask

class AnchorTaskOne : AnchorTask(StartUpKey.MUST_BE_ONE) {
    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        BaseStartUpManager.instance().delayInit(AppApplication.instance())
        AppStartUpManager.instance().delayInit(AppApplication.instance())
    }
}