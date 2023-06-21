package com.wx.detalk.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SizeUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.wx.base.arouter.ARouterMap
import com.wx.base.util.Constant
import com.wx.base.util.ToastUtil
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.util.AppUtil
import com.wx.common.util.FileUtil
import com.wx.common.util.GlideUtils
import com.wx.detalk.R
import com.wx.detalk.databinding.ActivityShareBinding
import com.wx.detalk.mvvm.viewmodel.mine.ShareViewModel
import com.wx.detalk.zxing.CodeUtils
import i18n
import java.io.File

/**
 * Created by huy  on 2023/6/20.
 */
@Route(path = ARouterMap.SHARE)
class ShareActivity : VbBaseActivity<ShareViewModel, ActivityShareBinding>(), OnClickListener {
    var qr = "0x1234567890"

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))

        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.Share)
        val qrBitmap = CodeUtils.createImage(qr, SizeUtils.dp2px(140f), SizeUtils.dp2px(140f), null)
        GlideUtils.instance?.loadImage(binding.ivQr, qrBitmap)
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.tv_save -> {
                val shareBitmap = AppUtil.getViewBp(binding.llShare)
                dowload(shareBitmap!!)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun dowload(bitmap: Bitmap) {
        var rxPermissions = RxPermissions(this@ShareActivity)
        rxPermissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
            .subscribe {
                if (it) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        FileUtil.saveBitmapMediaStore(bitmap)
                    } else {
                        val appDir =
                            File(Environment.getExternalStorageDirectory(), Constant.DETALK_FILE_PATH)
                        FileUtils.createOrExistsDir(appDir)
                        FileUtil.saveBitmap(bitmap, FileUtil.getPath() + "/detalk/img/")
                    }
                    ToastUtil.showToast("Download succeeded")
                } else {
                    ToastUtil.showToast("Download Failed")
                }
            }
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityShareBinding.inflate(layoutInflater)
    }

}