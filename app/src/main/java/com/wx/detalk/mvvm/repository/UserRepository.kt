package com.wx.detalk.mvvm.repository

import com.wx.base.bean.HttpWrapper
import com.wx.base.mvvm.base.BaseRepository

/**
 * Created by huy  on 2023/6/16.
 */
class UserRepository : BaseRepository() {

    suspend fun login(param: Map<String, String>): HttpWrapper<String> {
        return mServer.login(param)
    }

}