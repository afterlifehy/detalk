package com.wx.common.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class KeyboardNumberBean : MultiItemEntity {
    override var itemType = 0
    var number: String = ""

    constructor() {}
    constructor(itemType: Int, number: String) {
        this.itemType = itemType
        this.number = number
    }
}