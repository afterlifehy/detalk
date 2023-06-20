package com.wx.common.bean

/**
 * Created by huy  on 2023/6/15.
 */
class ConfirmBean {
    var topic = ""
    var content = ""

    constructor()

    constructor(topic: String, content: String) {
        this.topic = topic
        this.content = content
    }
}