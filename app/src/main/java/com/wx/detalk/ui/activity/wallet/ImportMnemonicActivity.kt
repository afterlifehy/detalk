package com.wx.detalk.ui.activity.wallet

import android.util.Base64
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.wx.base.BaseApplication
import com.wx.base.ext.i18N
import com.wx.base.viewbase.VbBaseActivity
import com.wx.base.arouter.ARouterMap
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.mvvm.base.UrlManager
import com.wx.base.util.ToastUtil
import com.wx.common.bean.LocalWalletBean
import com.wx.common.event.AddWalletSuccessEvent
import com.wx.common.realm.RealmUtil
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.databinding.ActivityImportMnemonicBinding
import com.wx.detalk.mvvm.viewmodel.wallet.ImportMnemonicViewModel
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import wallet.core.jni.AES
import wallet.core.jni.AESPaddingMode
import wallet.core.jni.HDWallet
import java.util.Collections
import kotlin.collections.ArrayList

/**
 * Created by huy  on 2022/8/22.
 */
@Route(path = ARouterMap.IMPORT_MNEMONIC)
class ImportMnemonicActivity : VbBaseActivity<ImportMnemonicViewModel, ActivityImportMnemonicBinding>(),
    View.OnClickListener {
    var wallet: HDWallet? = null

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18N(com.wx.base.R.string.import_wallet)

        initListener()
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.rtvConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.rtv_confirm -> {
                var mnemonic = binding.retMnemonic.text.toString()
                val mnemonicTempList = mnemonic.replace("\n", " ").split(" ")
                (mnemonicTempList as ArrayList).removeAll(Collections.singleton(""))
                if (mnemonicTempList.size != 12) {
                    ToastUtils.showShort("SECRET PHRASE ERROR")
                    return
                }
                showProgressDialog()
                mnemonic =
                    mnemonicTempList.toString().substring(1, mnemonicTempList.toString().length - 1).replace(",", "")
                wallet = Web3jUtil.instance?.importWallet(mnemonic, "")
                Web3jUtil.instance?.buildWeb3j(UrlManager.getWeb3jHttpUrl())
                Web3jUtil.instance?.getWalletAddress()
                if (wallet != null) {
                    addWalletAddress(wallet!!.getAddressForCoin(Web3jUtil.instance?.myCoinType))
                } else {
                    dismissProgressDialog()
                }
            }
        }
    }

    override fun initData() {

    }

    private fun addWalletAddress(walletAddress: String) {
        runBlocking {
            PreferencesDataStore(BaseApplication.instance()).putString(
                PreferencesKeys.wallet_address,
                wallet!!.getAddressForCoin(Web3jUtil.instance?.myCoinType)
            )
            val code =
                PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.securityCode)
            val wallet_address =
                PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.wallet_address)
            val chainId = Web3jUtil.instance?.CHAIN_ID
            val data = Base64.encodeToString(
                AES.encryptCBC(
                    code.toByteArray(),
                    wallet!!.mnemonic()?.toByteArray(),
                    code.toByteArray(),
                    AESPaddingMode.PKCS7
                ),
                Base64.DEFAULT
            )
            val localWalletBean = LocalWalletBean(wallet_address, code, data, chainId!!, System.currentTimeMillis())
            RealmUtil.instance?.addRealm(localWalletBean)
            finish()
            EventBus.getDefault().post(AddWalletSuccessEvent())
        }
        ToastUtils.showShort(i18N(com.wx.base.R.string.Import_succeeded))
        finish()
//        val param = HashMap<String, String>()
//        param["getWalletAddress"] = walletAddress
//        mViewModel.addWalletAddress(param)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
//            addWalletAddress.observe(this@ImportMnemonicActivity) {
//                dismissProgressDialog()
//                if (it) {
//
//                }
//            }
            errMsg.observe(this@ImportMnemonicActivity) {
                dismissProgressDialog()
                Web3jUtil.instance?.clearWallet()
                ToastUtil.showToast(it.msg)
            }
        }
    }

    override fun providerVMClass(): Class<ImportMnemonicViewModel> {
        return ImportMnemonicViewModel::class.java
    }


    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityImportMnemonicBinding.inflate(layoutInflater)
    }

}