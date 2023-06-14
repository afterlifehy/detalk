package com.wx.base.mvvm.base

import com.wx.base.BuildConfig

object UrlManager {
    const val DEV_HOST = "https://api.openai.com/v1/"
    const val FORMAL_HOST = "https://api.openai.com/v1/"

    fun getServerUrl(): String {
        if (BuildConfig.is_dev) {
            return DEV_HOST
        } else {
            return FORMAL_HOST
        }
    }
}