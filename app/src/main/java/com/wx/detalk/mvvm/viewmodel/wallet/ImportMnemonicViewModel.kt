package com.wx.detalk.mvvm.viewmodel.wallet

import com.wx.base.mvvm.base.BaseViewModel

/**
 * Created by huy  on 2022/8/22.
 */
class ImportMnemonicViewModel: BaseViewModel() {

//    val mWalletAccountRepository by lazy {
//        WalletAccountRepository()
//    }
//
//    val addWalletAddress = MutableLiveData<Boolean>()
//
//    /**
//     * 添加钱包地址
//     */
//    fun addWalletAddress(par: Map<String, String>) {
//
//        launch {
//            val response = withContext(Dispatchers.IO) {
//                mWalletAccountRepository.addWalletAddress(par)
//            }
//            executeResponse(response, {
//                addWalletAddress.value = response.data
//            }, {
//                traverseErrorMsg(ErrorMessage(msg = response.msg, code = 1002))
//            })
//        }
//    }
}