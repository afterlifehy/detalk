package com.wx.detalk.mvvm.viewmodel.home

import androidx.lifecycle.MutableLiveData
import com.wx.base.mvvm.base.BaseViewModel
import com.wx.base.mvvm.base.ErrorMessage
import com.wx.common.bean.LocalWalletBean
import com.wx.common.realm.RealmUtil
import com.wx.detalk.mvvm.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by huy  on 2023/6/14.
 */
class HomeViewModel : BaseViewModel() {

    val mUserRepository by lazy {
        UserRepository()
    }
    val loginLiveData = MutableLiveData<String>()

    /**
     *登录
     */
    fun login(param: Map<String, String>) {

        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.login(param)
            }
            executeResponse(response, {
                loginLiveData.value = response.data
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.code))
            })
        }
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