package com.wx.common.util

import androidx.collection.ArrayMap
import i18n

/**
 * Created by huy  on 2022/8/15.
 */
object AppFlag {

    val securityDescribeMap: MutableMap<Int, String> = ArrayMap()

    init {
        securityDescribeMap[1] = i18n(com.wx.base.R.string.create_your_passcode)
        securityDescribeMap[2] = i18n(com.wx.base.R.string.confirm_your_passcode)
        securityDescribeMap[3] = i18n(com.wx.base.R.string.enter_your_passcode)
    }

    val bscChainIdMap: MutableMap<Boolean, Long> = android.util.ArrayMap()
    val ethChainIdMap: MutableMap<Boolean, Long> = android.util.ArrayMap()

    init {
        bscChainIdMap[true] = 97L
        bscChainIdMap[false] = 56L

        ethChainIdMap[true] = 5L
        ethChainIdMap[false] = 1L
    }



    val enMonth = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AGU", "SEP", "OCT", "NOV", "DEC")

    val enMonthList: MutableList<String> = ArrayList()

    init {
        enMonthList.add("JAN")
        enMonthList.add("FEB")
        enMonthList.add("MAR")
        enMonthList.add("APR")
        enMonthList.add("MAY")
        enMonthList.add("JUN")
        enMonthList.add("JUL")
        enMonthList.add("AGU")
        enMonthList.add("SEP")
        enMonthList.add("OCT")
        enMonthList.add("NOV")
        enMonthList.add("DEC")
    }

    val enMonthMap: MutableMap<String, Int> = ArrayMap()

    init {
        enMonthMap["JAN"] = 1
        enMonthMap["FEB"] = 2
        enMonthMap["MAR"] = 3
        enMonthMap["APR"] = 4
        enMonthMap["MAY"] = 5
        enMonthMap["JUN"] = 6
        enMonthMap["JUL"] = 7
        enMonthMap["AGU"] = 8
        enMonthMap["SEP"] = 9
        enMonthMap["OCT"] = 10
        enMonthMap["NOV"] = 11
        enMonthMap["DEC"] = 12
    }


    val sybolMap:MutableMap<String,String> = ArrayMap()
    init {
        sybolMap["-"] = "Expense"
        sybolMap["+"] = "Income"
    }
}