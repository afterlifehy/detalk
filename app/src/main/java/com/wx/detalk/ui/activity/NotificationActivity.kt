package com.wx.detalk.ui.activity

import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.wx.base.arouter.ARouterMap
import com.wx.base.viewbase.VbBaseActivity
import com.wx.detalk.R
import com.wx.detalk.adapter.ApproveNotificationAdapter
import com.wx.detalk.adapter.LeaveNotificationAdapter
import com.wx.detalk.databinding.ActivityNotificationBinding
import com.wx.detalk.mvvm.viewmodel.home.NotificationViewModel
import i18n

/**
 * Created by huy  on 2023/6/15.
 */
@Route(path = ARouterMap.NOTIFICATION)
class NotificationActivity : VbBaseActivity<NotificationViewModel, ActivityNotificationBinding>(), OnClickListener {
    var approveList: MutableList<Int> = ArrayList()
    var approveNotificationAdapter: ApproveNotificationAdapter? = null

    var leaveList: MutableList<String> = ArrayList()
    var leaveNotificationAdapter: LeaveNotificationAdapter? = null

    override fun initView() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, com.wx.base.R.color.transparent))
        binding.layoutToolbar.tvTitle.text = i18n(com.wx.base.R.string.Notification)

        binding.rvApproveNotification.isNestedScrollingEnabled = false
        binding.rvApproveNotification.setHasFixedSize(true)
        binding.rvApproveNotification.layoutManager = LinearLayoutManager(this@NotificationActivity)
        approveNotificationAdapter = ApproveNotificationAdapter(approveList, this@NotificationActivity)
        binding.rvApproveNotification.adapter = approveNotificationAdapter

        binding.rvLeaveNotification.isNestedScrollingEnabled = false
        binding.rvLeaveNotification.setHasFixedSize(true)
        binding.rvLeaveNotification.layoutManager = LinearLayoutManager(this@NotificationActivity)
        leaveNotificationAdapter = LeaveNotificationAdapter(leaveList)
        binding.rvLeaveNotification.adapter = leaveNotificationAdapter
    }

    override fun initListener() {
        binding.layoutToolbar.ivBack.setOnClickListener(this)
    }

    override fun initData() {
        approveList.add(0)
        approveList.add(0)
        approveList.add(0)
        approveList.add(0)
        approveList.add(0)
        approveNotificationAdapter?.setList(approveList)

        leaveList.add("David leave manner coffee club 3mins ago")
        leaveList.add("David leave manner coffee club 3mins ago")
        leaveList.add("David leave")
        leaveList.add("David leave manner coffee club 3mins ago")
        leaveList.add("David leave manner coffee club 3mins ago")
        leaveNotificationAdapter?.setList(leaveList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressedSupport()
            }

            R.id.rtv_approve -> {
                val approveView = v as TextView
                approveView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@NotificationActivity,
                        com.wx.base.R.color.transparent
                    )
                )
                approveView.setTextColor(
                    ContextCompat.getColor(
                        this@NotificationActivity,
                        com.wx.base.R.color.color_ffffee00
                    )
                )
                approveView.text = i18n(com.wx.base.R.string.Agreed)
                approveView.setOnClickListener(null)
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityNotificationBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

}