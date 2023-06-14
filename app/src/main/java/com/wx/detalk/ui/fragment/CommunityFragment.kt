package com.wx.detalk.ui.fragment

import androidx.viewbinding.ViewBinding
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.databinding.FragmentCommunityBinding
import com.wx.detalk.mvvm.viewmodel.community.CommunityViewModel

/**
 * Created by huy  on 2023/6/14.
 */
class CommunityFragment: VbBaseFragment<CommunityViewModel, FragmentCommunityBinding>() {
    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentCommunityBinding.inflate(layoutInflater)
    }

}