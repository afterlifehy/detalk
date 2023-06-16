package com.wx.common.bean

import com.wx.common.R

/**
 * Created by huy  on 2023/6/15.
 */
class NetworkBean {
    var logo: Int = R.mipmap.ic_eth
    var name = ""

    constructor()

    constructor(logo: Int, name: String) {
        this.logo = logo
        this.name = name
    }
}