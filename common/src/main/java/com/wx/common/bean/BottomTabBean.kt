package com.wx.common.bean

import androidx.fragment.app.Fragment

class BottomTabBean{
    var tabName: String = ""
    var mFragment: Fragment?= null
    var imageAssetsFolder: String = ""
    var mAnimationName: String = ""
    var checkedImg:Int = 0
    var unCheckedImg:Int= 0

    constructor()
    constructor(tabName: String, mFragment: Fragment?, checkedImg: Int, unCheckedImg: Int) {
        this.tabName = tabName
        this.mFragment = mFragment
        this.checkedImg = checkedImg
        this.unCheckedImg = unCheckedImg
    }

}