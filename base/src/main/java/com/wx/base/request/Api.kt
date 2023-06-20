package com.wx.base.request

import com.wx.base.bean.HttpWrapper
import retrofit2.http.*


interface Api {

    /**
     *登录
     */
    @POST("user/open/login")
    suspend fun login(@Body param: Map<String, String>): HttpWrapper<String>

}