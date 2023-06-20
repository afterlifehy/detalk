package com.wx.detalk.ui.activity.wallet

import com.wx.common.bean.LocalWalletBean

import android.content.Intent
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.wx.base.BaseApplication
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.KeyboardNumberBean
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.common.realm.RealmUtil
import com.wx.base.arouter.ARouterMap
import com.wx.base.util.Constant
import com.wx.base.util.ToastUtil
import com.wx.common.util.AppFlag
import com.wx.detalk.R
import com.wx.detalk.adapter.wallet.KeyboardAdapter
import com.wx.detalk.databinding.ActivitySecurityBinding
import com.wx.detalk.mvvm.viewmodel.wallet.SecurityViewModel
import io.realm.Realm
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import wallet.core.jni.AES
import wallet.core.jni.AESPaddingMode

/**
 * Created by huy  on 2022/8/23.
 */
@Route(path = ARouterMap.SECURITY)
class SecurityActivity : VbBaseActivity<SecurityViewModel, ActivitySecurityBinding>(), View.OnClickListener {
    var tempCode = ""
    var keyboardAdapter: KeyboardAdapter? = null
    var securityType = Constant.SECURITY_COMMON
    var securityDestination = ""
    var paramKey = ""
    var paramValue = -1
    var createTempCode = ""
    var securityFragment = false
    var isPost = false

    var keyboradNumberList = object : ArrayList<MultiItemEntity>() {
        init {
            add(KeyboardNumberBean(0, "1"))
            add(KeyboardNumberBean(0, "2"))
            add(KeyboardNumberBean(0, "3"))
            add(KeyboardNumberBean(0, "4"))
            add(KeyboardNumberBean(0, "5"))
            add(KeyboardNumberBean(0, "6"))
            add(KeyboardNumberBean(0, "7"))
            add(KeyboardNumberBean(0, "8"))
            add(KeyboardNumberBean(0, "9"))
            add(KeyboardNumberBean(0, ""))
            add(KeyboardNumberBean(0, "0"))
            add(KeyboardNumberBean(1, ""))
        }
    }

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        initListener()

        securityType = intent.getIntExtra(ARouterMap.SECURITY_TYPE, Constant.SECURITY_COMMON)
        if (intent.getStringExtra(ARouterMap.SECURITY_DESTINATION) != null) {
            securityDestination = intent.getStringExtra(ARouterMap.SECURITY_DESTINATION).toString()
        }
        if (!intent.getStringExtra(ARouterMap.SECURITY_PARAM_KEY).isNullOrEmpty()) {
            paramKey = intent.getStringExtra(ARouterMap.SECURITY_PARAM_KEY).toString()
            paramValue = intent.getIntExtra(ARouterMap.SECURITY_PARAM_VALUE, -1)
        }
        securityFragment = intent.getBooleanExtra(ARouterMap.SECURITY_FRAMENT, false)
        initSecuredView()

        binding.rvKeyBoard.setHasFixedSize(true)
        binding.rvKeyBoard.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        keyboardAdapter = KeyboardAdapter(keyboradNumberList, this)
        binding.rvKeyBoard.adapter = keyboardAdapter
    }

    private fun initSecuredView() {
        binding.tvDescribe.text = AppFlag.securityDescribeMap[securityType]
        if (securityType == Constant.SECURITY_COMMON) binding.tvForgetWord.show() else binding.tvForgetWord.gone()
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.tvForgetWord.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }
            R.id.tv_forgetWord -> {
                ARouter.getInstance().build(ARouterMap.RESTOREPW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                finish()
            }
            R.id.rtv_number -> {
                if (tempCode.length == 6) {
                    return
                }
                val keyboardNumberBean = v.tag as KeyboardNumberBean
                tempCode += keyboardNumberBean.number
                when (tempCode.length) {
                    1 -> {
                        binding.cbCode1.isChecked = true
                    }
                    2 -> {
                        binding.cbCode2.isChecked = true
                    }
                    3 -> {
                        binding.cbCode3.isChecked = true
                    }
                    4 -> {
                        binding.cbCode4.isChecked = true
                    }
                    5 -> {
                        binding.cbCode5.isChecked = true
                    }
                    6 -> {
                        binding.cbCode6.isChecked = true
                    }
                }
                if (tempCode.length < 6) {
                    return
                }
                when (securityType) {
                    Constant.SECURITY_CREATE -> {
                        createTempCode = tempCode
                        tempCode = ""
                        clearInput(false)
                        securityType = Constant.SECURITY_SECOND_CONFIRM
                        initSecuredView()
                    }
                    Constant.SECURITY_SECOND_CONFIRM -> {
                        if (TextUtils.equals(tempCode, createTempCode)) {
                            runBlocking {
                                val address =
                                    PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.wallet_address)
                                val localWalletList = RealmUtil.instance?.findWalletByAddress(address)
                                if (localWalletList?.size == 1) {
                                    val localWalletBean = localWalletList[0] as LocalWalletBean
                                    val mnemonicAes = localWalletBean.mnemonicAes
                                    var code = localWalletBean.passCode
                                    val mnemonic = AES.decryptCBC(
                                        code.toByteArray(),
                                        Base64.decode(mnemonicAes, Base64.DEFAULT),
                                        code.toByteArray(),
                                        AESPaddingMode.PKCS7
                                    )
                                    code = EncryptUtils.encryptMD5ToString(tempCode)
                                    val data = Base64.encodeToString(
                                        AES.encryptCBC(
                                            code.toByteArray(),
                                            mnemonic,
                                            code.toByteArray(),
                                            AESPaddingMode.PKCS7
                                        ),
                                        Base64.DEFAULT
                                    )
                                    Realm.Transaction {
                                        localWalletBean.mnemonicAes = data
                                        localWalletBean.passCode = code
                                        RealmUtil.instance?.addRealmAsync(localWalletBean)
                                    }
                                }
                                PreferencesDataStore(BaseApplication.instance()).putString(
                                    PreferencesKeys.securityCode,
                                    EncryptUtils.encryptMD5ToString(tempCode)
                                )

                                if (securityDestination.isNotEmpty()) {
                                    if (paramKey.isNotEmpty()) {
                                        ARouter.getInstance().build(securityDestination)
                                            .withInt(paramKey, paramValue)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .navigation()
                                    } else {
                                        ARouter.getInstance().build(securityDestination)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .navigation()
                                    }
                                }
                                finish()
                                ToastUtils.showShort("Set succeeded")
                            }
                        } else {
                            clearInput(true)
                        }
                    }
                    Constant.SECURITY_COMMON -> {
                        runBlocking {
                            try {
                                val savedCode =
                                    PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.securityCode)
                                if (TextUtils.equals(savedCode, EncryptUtils.encryptMD5ToString(tempCode))) {
                                    if (securityDestination.isNotEmpty()) {
                                        if (paramKey.isNotEmpty()) {
                                            ARouter.getInstance().build(securityDestination)
                                                .withInt(paramKey, paramValue)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .navigation()
//                                            if (ARouterMap.TRANSFER == securityDestination) {
//                                                Constant.securityIsFirst = false
//                                            }
                                        } else {
                                            ARouter.getInstance().build(securityDestination)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .navigation()
                                        }
                                    } else {
                                        if (securityFragment) {
                                            isPost = true
                                        }
                                    }
                                    finish()
                                } else {
                                    clearInput(true)
                                }
                            } catch (e: Exception) {
                                ToastUtil.showToast(e.message.toString())
                                ClipboardUtils.copyText(e.message.toString())
                            }
                        }

                    }
                }
            }
            R.id.rfl_delete -> {
                if (tempCode.isEmpty()) {
                    return
                }
                tempCode = tempCode.substring(0, tempCode.length - 1)
                when (tempCode.length) {
                    0 -> {
                        binding.cbCode1.isChecked = false
                    }
                    1 -> {
                        binding.cbCode2.isChecked = false
                    }
                    2 -> {
                        binding.cbCode3.isChecked = false
                    }
                    3 -> {
                        binding.cbCode4.isChecked = false
                    }
                    4 -> {
                        binding.cbCode5.isChecked = false
                    }
                    5 -> {
                        binding.cbCode6.isChecked = false
                    }
                }
            }
        }
    }

    fun clearInput(isAnimation: Boolean) {
        tempCode = ""
        if (isAnimation) {
            translateAnimation(binding.llCode)
        }
        binding.cbCode1.isChecked = false
        binding.cbCode2.isChecked = false
        binding.cbCode3.isChecked = false
        binding.cbCode4.isChecked = false
        binding.cbCode5.isChecked = false
        binding.cbCode6.isChecked = false
    }

    override fun initData() {
    }


    private fun translateAnimation(view: View): TranslateAnimation {
        val animation =
            TranslateAnimation((-SizeUtils.dp2px(3f)).toFloat(), SizeUtils.dp2px(3f).toFloat(), 0f, 0f)
        animation.duration = 100L
        animation.fillAfter = false
        animation.repeatCount = 2
        view.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
        return animation
    }

    override fun isRegEventBus(): Boolean {
        return true
    }

    override fun providerVMClass(): Class<SecurityViewModel> {
        return SecurityViewModel::class.java
    }

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }


    override val isFullScreen: Boolean
        get() = true

    override fun getVbBindingView(): ViewBinding {
        return ActivitySecurityBinding.inflate(layoutInflater)
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }
}