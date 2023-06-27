package com.wx.detalk.ui.activity.wallet

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.wx.base.BaseApplication
import com.wx.base.ext.gone
import com.wx.base.ext.i18N
import com.wx.base.ext.show
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.LocalWalletBean
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.common.realm.RealmUtil
import com.wx.base.arouter.ARouterMap
import com.wx.base.mvvm.base.UrlManager
import com.wx.base.util.Constant
import com.wx.common.event.AddWalletSuccessEvent
import com.wx.common.util.Web3jUtil
import com.wx.detalk.R
import com.wx.detalk.adapter.wallet.ConfirmMnemonicAdapter
import com.wx.detalk.adapter.wallet.MnemonicAdapter
import com.wx.detalk.adapter.wallet.RandomMnemonicAdapter
import com.wx.detalk.databinding.ActivitySeedPhraseBinding
import com.wx.detalk.mvvm.viewmodel.wallet.SeedPhraseViewModel
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import wallet.core.jni.AES
import wallet.core.jni.AESPaddingMode
import wallet.core.jni.HDWallet

/**
 * Created by huy  on 2022/8/19.
 */
@Route(path = ARouterMap.SEED_PHRASE)
class SeedPhraseActivity : VbBaseActivity<SeedPhraseViewModel, ActivitySeedPhraseBinding>(), View.OnClickListener {
    var mnemonicAdapter: MnemonicAdapter? = null
    var mnemonicList: MutableList<String> = ArrayList()

    var randomMnemonicAdapter: RandomMnemonicAdapter? = null
    var randomMnemonicList: MutableList<String> = ArrayList()
    var mnemonicCheckedList: BooleanArray? = null
    var confirmMnemonicAdapter: ConfirmMnemonicAdapter? = null
    var confirmMnemonicList: MutableList<String> = ArrayList()
    var confirmCount = 0
    var type = Constant.SEED_PHRASE_SHOW
    var wallet: HDWallet? = null
    var address = ""

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18N(com.wx.base.R.string.SEED_PHRASE)

        type = intent.getIntExtra(ARouterMap.SEED_PHRASE_TYPE, Constant.SEED_PHRASE_SHOW)
        if (type == Constant.SEED_PHRASE_SHOW) {
            binding.layoutShow.tvWriteDown.show()

            binding.layoutShow.rvSeedPhrase.setHasFixedSize(true)
            binding.layoutShow.rvSeedPhrase.layoutManager = LinearLayoutManager(this)
            mnemonicAdapter = MnemonicAdapter(mnemonicList, true)
            binding.layoutShow.rvSeedPhrase.adapter = mnemonicAdapter

            binding.layoutConfirm.rvConfirmMnemonic.setHasFixedSize(true)
            binding.layoutConfirm.rvConfirmMnemonic.layoutManager =
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            confirmMnemonicAdapter = ConfirmMnemonicAdapter(confirmMnemonicList, this)
            binding.layoutConfirm.rvConfirmMnemonic.adapter = confirmMnemonicAdapter

            binding.layoutConfirm.rvRandomMnemonic.setHasFixedSize(true)
            binding.layoutConfirm.rvRandomMnemonic.layoutManager =
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            randomMnemonicAdapter = RandomMnemonicAdapter(randomMnemonicList, this)
            binding.layoutConfirm.rvRandomMnemonic.adapter = randomMnemonicAdapter
        } else if (type == Constant.SEED_PHRASE_BACKUP) {
            address = intent.getStringExtra(ARouterMap.SEED_PHRASE_ADDRESS).toString()
            binding.layoutShow.tvHold.show()

            binding.layoutShow.rvSeedPhrase.setHasFixedSize(true)
            binding.layoutShow.rvSeedPhrase.layoutManager = LinearLayoutManager(this)
            mnemonicAdapter = MnemonicAdapter(mnemonicList, false)
            binding.layoutShow.rvSeedPhrase.adapter = mnemonicAdapter
        }

        initListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
        binding.layoutShow.tvWriteDown.setOnClickListener(this)
        binding.layoutConfirm.rtvConfirm.setOnClickListener(this)
        binding.layoutShow.tvHold.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                }

                MotionEvent.ACTION_UP -> {
                    mnemonicAdapter?.setIsShow(false)
                }

                MotionEvent.ACTION_CANCEL -> {

                }
            }
            false
        }
        binding.layoutShow.tvHold.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                mnemonicAdapter?.setIsShow(true)
                return true
            }

        })
    }

    override fun initData() {
        if (type == Constant.SEED_PHRASE_SHOW) {
            wallet = Web3jUtil.instance?.createWallet("")
            runBlocking {
                PreferencesDataStore(BaseApplication.instance()).putLong(PreferencesKeys.chainId, Constant.chainId)
            }
            Web3jUtil.instance?.setChainId(Constant.chainId)
            Web3jUtil.instance?.buildWeb3j(UrlManager.getWeb3jHttpUrl())
            Web3jUtil.instance?.getWalletAddress()
            runBlocking {
                PreferencesDataStore(BaseApplication.instance()).putString(
                    PreferencesKeys.wallet_address,
                    wallet!!.getAddressForCoin(Web3jUtil.instance?.myCoinType)
                )
            }
            if (wallet != null) {
                mnemonicList = wallet!!.mnemonic().split(" ") as MutableList<String>
                mnemonicAdapter?.setNewInstance(mnemonicList)
            }
        } else if (type == Constant.SEED_PHRASE_BACKUP) {
            val localWalletBean = RealmUtil.instance?.findWalletByAddress(address)!![0]
            val mnemonicAes = localWalletBean.mnemonicAes
            val code = localWalletBean.passCode
            val mnemonic = AES.decryptCBC(
                code.toByteArray(),
                Base64.decode(mnemonicAes, Base64.DEFAULT),
                code.toByteArray(),
                AESPaddingMode.PKCS7
            )
            wallet = HDWallet(String(mnemonic), "")
            mnemonicList = wallet!!.mnemonic().split(" ") as MutableList<String>
            mnemonicAdapter?.setNewInstance(mnemonicList)
        }
    }

    fun initConfirmMnemonic() {
        randomMnemonicList = mnemonicList.shuffled() as MutableList<String>
        mnemonicCheckedList = BooleanArray(12)
        randomMnemonicAdapter?.setCheckedList(mnemonicCheckedList!!)
        randomMnemonicAdapter?.setNewInstance(randomMnemonicList)

        for (i in randomMnemonicList.indices) {
            confirmMnemonicList.add("")
        }
        confirmMnemonicAdapter?.setNewInstance(confirmMnemonicList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.tv_writeDown -> {
                binding.layoutConfirm.rlConfirm.show()
                binding.layoutShow.rlShow.gone()
                initConfirmMnemonic()
            }

            R.id.rtv_confirm -> {
                val mnemonicListString = GsonUtils.toJson(mnemonicList)
                val confirmMnemonicListString = GsonUtils.toJson(confirmMnemonicList)
                showProgressDialog()
                if (TextUtils.equals(mnemonicListString, confirmMnemonicListString)) {
                    runBlocking {
                        val address =
                            PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.wallet_address)
                        val code =
                            PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.securityCode)
                        val chainId = Web3jUtil.instance?.CHAIN_ID
                        val data = Base64.encodeToString(
                            AES.encryptCBC(
                                code.toByteArray(),
                                wallet?.mnemonic()?.toByteArray(),
                                code.toByteArray(),
                                AESPaddingMode.PKCS7
                            ),
                            Base64.DEFAULT
                        )
                        val localWalletBean =
                            LocalWalletBean(address, code, data, chainId!!, System.currentTimeMillis())
                        RealmUtil.instance?.addRealm(localWalletBean)
                        EventBus.getDefault().post(AddWalletSuccessEvent())
                        addWalletAddress()
                    }
                } else {
                    dismissProgressDialog()
                    ToastUtils.showShort("Mnemonic error")
                }
            }

            R.id.rtv_randomMnemonic -> {
                val random = v.tag as String
                val index = randomMnemonicList.indexOf(random)
                val isChecked = mnemonicCheckedList!![index]
                if (!isChecked) {
                    mnemonicCheckedList?.set(index, !isChecked)
                    randomMnemonicAdapter?.notifyItemChanged(index)
                    confirmMnemonicList[confirmCount] = random
                    confirmMnemonicAdapter?.notifyItemChanged(confirmCount)
                    confirmCount++
                }
            }

            R.id.rtv_confirmMnemonic -> {
                val confirm = v.tag as String
                if (confirm.isEmpty()) {
                    return
                }
                val index = confirmMnemonicList.indexOf(confirm)
                if (index < 0) {
                    return
                }
                confirmMnemonicList.removeAt(index)
                confirmMnemonicList.add(11, "")
                confirmMnemonicAdapter?.notifyItemRemoved(index)
                confirmCount--
                mnemonicCheckedList?.set(randomMnemonicList.indexOf(confirm), false)
                randomMnemonicAdapter?.notifyItemChanged(randomMnemonicList.indexOf(confirm))
            }
        }
    }

    private fun addWalletAddress() {
//        runBlocking {
//            val walletAddress =
//                PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.wallet_address)
//            val param = HashMap<String, String>()
//            param["getWalletAddress"] = walletAddress
//            mViewModel.addWalletAddress(param)
//        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
//            addWalletAddress.observe(this@SeedPhraseActivity) {
//                dismissProgressDialog()
//                if (it) {
//                    ARouter.getInstance().build(ARouterMap.ACCOUNT).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .navigation()
//                    finish()
//                }
//            }
        }
    }

    override fun isRegEventBus(): Boolean {
        return true
    }

    override fun providerVMClass(): Class<SeedPhraseViewModel> {
        return SeedPhraseViewModel::class.java
    }

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override val isFullScreen: Boolean
        get() = true

    override fun getVbBindingView(): ViewBinding {
        return ActivitySeedPhraseBinding.inflate(layoutInflater)
    }

    override fun onBackPressedSupport() {
        if (type == Constant.SEED_PHRASE_SHOW) {
            if (binding.layoutConfirm.rlConfirm.visibility == View.VISIBLE) {
                binding.layoutConfirm.rlConfirm.gone()
                binding.layoutShow.rlShow.show()
                confirmCount = 0
                randomMnemonicList.clear()
                confirmMnemonicList.clear()
                confirmMnemonicAdapter?.notifyDataSetChanged()
            } else {
                Web3jUtil.instance?.clearWallet()
                super.onBackPressedSupport()
            }
        } else if (type == Constant.SEED_PHRASE_BACKUP) {
            super.onBackPressedSupport()
        }
    }
}