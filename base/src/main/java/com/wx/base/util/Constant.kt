package com.wx.base.util

import com.wx.base.BuildConfig

object Constant {

    const val crtStr = ""
    const val h5CrtStr = ""
    const val timeOut = 100L
    const val DETALK_FILE_PATH = "/detalk"
    const val secret = "uio8e98u"

    const val CREATE_WALLET = 0
    const val IMPORT_WALLET = 1

    val SECURITY_CREATE = 1
    val SECURITY_SECOND_CONFIRM = 2
    val SECURITY_COMMON = 3

    val SEED_PHRASE_SHOW = 1
    val SEED_PHRASE_BACKUP = 2

    val DEV_CONTRACT_ADDRESS = ""
    val FORMAL_CONTRACT_ADDRESS = ""
    fun getContractAddress(): String {
        if (BuildConfig.is_dev) {
            return DEV_CONTRACT_ADDRESS
        } else {
            return FORMAL_CONTRACT_ADDRESS
        }
    }

    val PROJECT_ADDRESS = ""
}

