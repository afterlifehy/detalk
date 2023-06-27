package com.wx.detalk.mvvm.viewmodel.wallet

import com.wx.base.mvvm.base.BaseViewModel
import com.wx.common.bean.LocalWalletBean
import com.wx.common.realm.RealmUtil

/**
 * Created by huy  on 2023/6/26.
 */
class DeleteWalletViewModel: BaseViewModel() {
    fun deleteWallet(wallet: LocalWalletBean) {
        RealmUtil.instance?.deleteRealm(wallet)
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
}