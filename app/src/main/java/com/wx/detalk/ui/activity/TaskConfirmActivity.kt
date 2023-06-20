package com.wx.detalk.ui.activity

import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.viewbase.VbBaseActivity
import com.wx.common.bean.ConfirmBean
import com.wx.detalk.R
import com.wx.detalk.adapter.ConfirmTaskAdapter
import com.wx.detalk.databinding.ActivityTaskConfirmBinding
import com.wx.detalk.mvvm.viewmodel.home.TaskConfirmViewModel
import i18n

/**
 * Created by huy  on 2023/6/16.
 */
@Route(path = ARouterMap.CONFIRM)
class TaskConfirmActivity : VbBaseActivity<TaskConfirmViewModel, ActivityTaskConfirmBinding>(), OnClickListener {
    var taskConfirmList: MutableList<ConfirmBean> = ArrayList()
    var confirmTaskAdapter: ConfirmTaskAdapter? = null


    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.Confirm)

        binding.rvConfirmInfo.setHasFixedSize(true)
        binding.rvConfirmInfo.layoutManager = LinearLayoutManager(this)
        confirmTaskAdapter = ConfirmTaskAdapter(taskConfirmList)
        binding.rvConfirmInfo.adapter = confirmTaskAdapter
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
    }

    override fun initData() {
        binding.tvType.text = i18n(com.wx.base.R.string.TASK)

        taskConfirmList.add(ConfirmBean("Wallet Address:", "Manner COFFE  CLUB"))
        taskConfirmList.add(ConfirmBean("Club Name:", "Manner COFFE  CLUB"))
        taskConfirmList.add(ConfirmBean("TASK Name:", "Manner COFFE  CLUB"))
        taskConfirmList.add(ConfirmBean("Subtask Title:", "Manner COFFE  CLUB"))
        taskConfirmList.add(ConfirmBean("Subtask Info:", "Manner COFFE  CLUB"))
        taskConfirmList.add(ConfirmBean("Time:", "20230101~20240101"))
        taskConfirmList.add(ConfirmBean("Eficient:", "Yes"))
        confirmTaskAdapter?.setList(taskConfirmList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityTaskConfirmBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

}