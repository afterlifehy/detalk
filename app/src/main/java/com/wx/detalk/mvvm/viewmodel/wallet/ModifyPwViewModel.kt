package com.wx.detalk.mvvm.viewmodel.wallet

import com.wx.base.mvvm.base.BaseViewModel
import com.wx.common.bean.LocalWalletBean
import com.wx.common.realm.RealmUtil

/**
 * Created by huy  on 2023/6/26.
 */
class ModifyPwViewModel : BaseViewModel() {
    fun findWalletByAddress(address: String): LocalWalletBean {
        return RealmUtil.instance?.findWalletByAddress(address)!![0]
    }

    fun updateWallet(localWalletBean: LocalWalletBean, passcode: String) {
        RealmUtil.instance?.updateWallet(localWalletBean, passcode, localWalletBean.timeStamp)
    }
}