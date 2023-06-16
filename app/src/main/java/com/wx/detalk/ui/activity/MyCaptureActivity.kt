package com.wx.detalk.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.wx.base.viewbase.VbBaseActivity
import com.wx.base.arouter.ARouterMap
import com.wx.detalk.R
import com.wx.detalk.databinding.ActivityMyCaptureBinding
import com.wx.detalk.mvvm.viewmodel.home.MyCaptureViewModel
import com.wx.detalk.ui.fragment.CaptureFragment
import com.wx.detalk.zxing.CodeUtils

/**
 * Created by huy  on 2022/8/23.
 */
@Route(path = ARouterMap.MYCAPTURE)
class MyCaptureActivity : VbBaseActivity<MyCaptureViewModel, ActivityMyCaptureBinding>(), View.OnClickListener,
    CodeUtils.AnalyzeCallback {
    var captureFragment: CaptureFragment? = null
    var fragmentManager: FragmentManager? = null
    var fragmentTransaction: FragmentTransaction? = null

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        fragmentManager = supportFragmentManager
        if (captureFragment == null) {
            captureFragment = CaptureFragment()
        }
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_mycapture)
        captureFragment?.analyzeCallback = this
        showFragment(fragmentManager!!, fragmentTransaction, captureFragment, R.id.fl_captureContent, "captureFragment")
    }

    override fun initListener() {

    }

    override fun initData() {
    }

    override fun onClick(v: View?) {

    }

    override fun onAnalyzeSuccess(mBitmap: Bitmap?, result: String?) {
        val intent = Intent()
        intent.putExtra("result", result)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onAnalyzeFailed() {
        ToastUtils.showShort("Scan Failed")
    }

    override fun marginStatusBarView(): View {
        return binding.rlToolbar
    }


    override val isFullScreen: Boolean
        get() = true

    override fun getVbBindingView(): ViewBinding {
        return ActivityMyCaptureBinding.inflate(layoutInflater)
    }

}