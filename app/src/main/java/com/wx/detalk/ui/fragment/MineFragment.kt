package com.wx.detalk.ui.fragment

import androidx.viewbinding.ViewBinding
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.databinding.FragmentMineBinding
import com.wx.detalk.mvvm.viewmodel.mine.MineViewModel

/**
 * Created by huy  on 2023/6/14.
 */
class MineFragment : VbBaseFragment<MineViewModel, FragmentMineBinding>() {
    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentMineBinding.inflate(layoutInflater)
    }

}