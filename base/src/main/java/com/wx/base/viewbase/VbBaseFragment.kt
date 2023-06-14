package com.wx.base.viewbase

import android.view.View
import androidx.viewbinding.ViewBinding
import com.wx.base.R
import com.wx.base.mvvm.base.BaseViewModel

/**
 * Created by zj on 2021/3/12.
 */
abstract class VbBaseFragment<VM : BaseViewModel, vb : ViewBinding> : BaseDataFragmentKt<VM>() {
    lateinit var binding: vb

    override fun getBindingView(): View? {
        val mBindind = getVbBindingView()
        binding = mBindind as vb
        return mBindind.root
    }


    abstract fun getVbBindingView(): ViewBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_vb_default_layout
    }
}