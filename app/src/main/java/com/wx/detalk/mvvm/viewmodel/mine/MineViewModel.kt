package com.wx.detalk.mvvm.viewmodel.mine

import com.wx.base.mvvm.base.BaseViewModel
import com.wx.common.bean.LocalWalletBean
import com.wx.common.realm.RealmUtil

/**
 * Created by huy  on 2023/6/14.
 */
class MineViewModel : BaseViewModel() {
    fun updateWallet(localWalletBean: LocalWalletBean, currentTimeMillis: Long) {
        RealmUtil.instance?.updateWallet(localWalletBean, localWalletBean.passCode, currentTimeMillis)
    }

    /**
     * 获取当前钱包
     */
    fun getCurrentWallet(): LocalWalletBean? {
        val walletList = RealmUtil.instance?.findWalletList()
        if (walletList.isNullOrEmpty()) {
            return null
        } else {
            return walletList[0]
        }
    }

    fun findWalletList(): List<LocalWalletBean>? {
        return RealmUtil.instance?.findWalletList()
    }
}