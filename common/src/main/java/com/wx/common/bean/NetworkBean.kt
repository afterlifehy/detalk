package com.wx.common.bean

import com.wx.common.R

/**
 * Created by huy  on 2023/6/15.
 */
class NetworkBean {
    var logo: Int = R.mipmap.ic_eth
    var name = ""
    var chainId = 1L

    constructor()

    constructor(logo: Int, name: String,chainId:Long) {
        this.logo = logo
        this.name = name
        this.chainId = chainId
    }
}