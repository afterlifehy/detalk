package com.wx.base.mvvm.base

import com.wx.base.bean.ResResponse
import com.wx.base.http.RetrofitUtils
import com.wx.base.request.Api


open class BaseRepository {

    val mServer by lazy {
        RetrofitUtils.getInstance().createCoroutineRetrofit(
            Api::class.java,
            UrlManager.getServerUrl()
        )
    }

    suspend fun <T : Any> apiCall(call: suspend () -> ResResponse<T>): ResResponse<T> {
        return call.invoke()
    }

}