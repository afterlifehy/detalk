package com.wx.detalk.mvvm.viewmodel

import com.wx.base.mvvm.base.BaseViewModel
import com.wx.common.bean.LocalWalletBean
import com.wx.common.realm.RealmUtil

/**
 * Created by huy  on 2023/6/14.
 */
class MainViewModel : BaseViewModel() {
    fun getCurrentWallet(): LocalWalletBean? {
        val walletList = RealmUtil.instance?.findWalletList()
        if (walletList.isNullOrEmpty()) {
            return null
        } else {
            return walletList[0]
        }
    }
}