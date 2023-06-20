package com.wx.common.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class LocalWalletBean : RealmObject {
    @PrimaryKey
    var address: String = ""
    var passCode: String = ""
    var mnemonicAes: String = ""
    var chainId: Long = 0L
    var timeStamp: Long = 0L

    constructor()
    constructor(address: String, passCode: String, mnemonicAes: String, chainId: Long, timeStamp: Long) {
        this.address = address
        this.passCode = passCode
        this.mnemonicAes = mnemonicAes
        this.chainId = chainId
        this.timeStamp = timeStamp
    }
}
