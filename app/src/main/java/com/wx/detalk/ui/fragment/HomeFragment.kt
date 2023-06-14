package com.wx.detalk.ui.fragment

import androidx.viewbinding.ViewBinding
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.databinding.FragmentHomeBinding
import com.wx.detalk.mvvm.viewmodel.home.HomeViewModel

/**
 * Created by huy  on 2023/6/14.
 */
class HomeFragment: VbBaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun getVbBindingView(): ViewBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
    }
}