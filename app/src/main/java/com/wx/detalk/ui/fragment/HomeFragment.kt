package com.wx.detalk.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.base.util.Constant
import com.wx.base.viewbase.VbBaseFragment
import com.wx.common.event.AddWalletSuccessEvent
import com.wx.detalk.R
import com.wx.detalk.adapter.BoxListAdapter
import com.wx.detalk.contract.ScanResultContract
import com.wx.detalk.databinding.FragmentHomeBinding
import com.wx.detalk.mvvm.viewmodel.home.HomeViewModel
import com.wx.detalk.ui.dialog.CreateWalletDialog
import com.wx.detalk.ui.pop.CreateBoxPop
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by huy  on 2023/6/14.
 */
class HomeFragment : VbBaseFragment<HomeViewModel, FragmentHomeBinding>(), OnClickListener {
    var boxListAdapter: BoxListAdapter? = null
    var boxList: MutableList<Int> = ArrayList()
    var createBoxPop: CreateBoxPop? = null
    var createWalletDialog: CreateWalletDialog? = null
    val scanResultLauncher = registerForActivityResult(ScanResultContract()) { reslut ->
        ARouter.getInstance().build(ARouterMap.CONFIRM).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(addWalletSuccessEvent: AddWalletSuccessEvent) {
        showView(2)
    }

    override fun initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(binding.layoutToolbar.ablToolbar)

        binding.layoutBoxList.rvBox.setHasFixedSize(true)
        binding.layoutBoxList.rvBox.layoutManager = LinearLayoutManager(BaseApplication.instance())
        boxListAdapter = BoxListAdapter(boxList, this)
        binding.layoutBoxList.rvBox.adapter = boxListAdapter

    }

    override fun initListener() {
        binding.layoutToolbar.ivScan.setOnClickListener(this)
        binding.layoutToolbar.ivAddBox.setOnClickListener(this)
        binding.layoutNoWallet.rtvGoNow.setOnClickListener(this)

        binding.layoutBoxList.srlBox.setOnRefreshListener {
            binding.layoutBoxList.srlBox.finishRefresh(5000)
        }
        binding.layoutBoxList.srlBox.setOnLoadMoreListener {
            binding.layoutBoxList.srlBox.finishLoadMore(5000)
        }
    }

    override fun initData() {
        val currentWallet = mViewModel.getCurrentWallet()
        if (currentWallet != null) {
            showView(2)
        } else {
            showView(0)
        }
        boxList.add(0)
        boxList.add(1)
        boxList.add(2)
        boxList.add(1)
        boxList.add(0)
        boxList.add(0)
        boxListAdapter?.setList(boxList)
    }

    @SuppressLint("CheckResult")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_scan -> {
                var rxPermissions = RxPermissions(requireActivity())
                rxPermissions.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                    .subscribe {
                        if (it) {
                            scanResultLauncher.launch("")
                        } else {

                        }
                    }
            }

            R.id.iv_addBox -> {
                createBoxPop = null
                createBoxPop = CreateBoxPop(requireActivity(), object : CreateBoxPop.CreateBoxPopCallBack {
                    override fun createDao() {
                    }

                    override fun createClub() {

                    }

                })
                createBoxPop?.showAsDropDown(v, -SizeUtils.dp2px(120f), v.y.toInt())
            }

            R.id.rtv_goNow -> {
//                val params = HashMap<String,String>()
//                params["wallet"] = "0x321regdsgfgrgsfaassadsd"
//                mViewModel.login(params)
                if (createWalletDialog == null) {
                    createWalletDialog = CreateWalletDialog(object : CreateWalletDialog.CreateWalletCallBack {
                        override fun createWallet() {
                            ARouter.getInstance().build(ARouterMap.SELECT_NETWORK)
                                .withInt(ARouterMap.ADD_WALLET_TYPE, Constant.CREATE_WALLET)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                        }

                        override fun importWallet() {
                            ARouter.getInstance().build(ARouterMap.SELECT_NETWORK)
                                .withInt(ARouterMap.ADD_WALLET_TYPE, Constant.IMPORT_WALLET)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                        }

                    })
                }
                createWalletDialog?.show()
            }

            R.id.rrl_box -> {
                val box = v.tag as Int
                if (box == 2) {
                    ARouter.getInstance().build(ARouterMap.NOTIFICATION).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation()
                }
            }
        }
    }

    fun showView(viewType: Int) {
        when (viewType) {
            0 -> {
                binding.layoutNoWallet.llNoWallet.show()
                binding.layoutNoBox.llNoBox.gone()
                binding.layoutBoxList.llBoxList.gone()
            }

            1 -> {
                binding.layoutNoWallet.llNoWallet.gone()
                binding.layoutNoBox.llNoBox.show()
                binding.layoutBoxList.llBoxList.gone()
            }

            2 -> {
                binding.layoutNoWallet.llNoWallet.gone()
                binding.layoutNoBox.llNoBox.gone()
                binding.layoutBoxList.llBoxList.show()
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            loginLiveData.observe(this@HomeFragment) {
                it
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun isRegEventBus(): Boolean {
        return true
    }

    override fun providerVMClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }


}