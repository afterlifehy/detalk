package com.wx.base.bean

data class HttpWrapper<out T>(val code: Int, val msg: String, val data: T)