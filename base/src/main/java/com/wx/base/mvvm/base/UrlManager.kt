package com.wx.base.mvvm.base

import com.wx.base.BaseApplication
import com.wx.base.BuildConfig
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import kotlinx.coroutines.runBlocking

object UrlManager {
    const val DEV_HOST = "http://47.243.113.236:30002/"
    const val FORMAL_HOST = "https://api.openai.com/v1/"

    const val DEV_WEB3J_HTTP_URL = "https://data-seed-prebsc-1-s1.binance.org:8545"
    const val FORMAL_WEB3J_HTTP_URL = "https://bsc-dataseed1.binance.org/"

    const val BSC_DEV_WEB3J_URL = "https://docs.bnbchain.org/docs/BSCtestnet"
    const val ETH_DEV_WEB3J_URL = ""

    var WEB3J_URL = ""

    const val BSC_FORMAL_WEB3J_URL =
        "https://rough-virulent-bridge.bsc.discover.quiknode.pro/bf4b738b8ebfa5725450ba18cba6e19ffb328d5d/"
    const val ETH_FORMAL_WEB3J_URL = "https://mainnet.infura.io/v3/5c8097ac1b7f4b3abbe254fd0c7894f3"

    fun getServerUrl(): String {
        if (BuildConfig.is_dev) {
            return DEV_HOST
        } else {
            return FORMAL_HOST
        }
    }

    fun getWeb3jHttpUrl(): String {

        runBlocking {
            val chainId = PreferencesDataStore(BaseApplication.instance()).getLong(PreferencesKeys.chainId)
            WEB3J_URL = when (chainId) {
                1L -> {
                    ETH_FORMAL_WEB3J_URL
                }

                5L -> {
                    ETH_DEV_WEB3J_URL
                }

                56L -> {
                    BSC_FORMAL_WEB3J_URL
                }

                97L -> {
                    BSC_DEV_WEB3J_URL
                }

                else -> {
                    ""
                }
            }
        }
        return WEB3J_URL
    }
}