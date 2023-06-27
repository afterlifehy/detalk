package com.wx.detalk.ui.fragment

import android.view.View
import android.view.View.OnClickListener
import androidx.viewbinding.ViewBinding
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.databinding.FragmentMineReplyBinding
import com.wx.detalk.mvvm.viewmodel.mine.MineReplyViewModel

/**
 * Created by huy  on 2023/6/26.
 */
class MineReplyFragment : VbBaseFragment<MineReplyViewModel, FragmentMineReplyBinding>(), OnClickListener {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun onClick(v: View?) {
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentMineReplyBinding.inflate(layoutInflater)
    }
}