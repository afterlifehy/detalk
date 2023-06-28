package com.wx.detalk.ui.fragment

import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.ui.view.radius.RadiusTextView
import com.wx.base.BaseApplication
import com.wx.base.arouter.ARouterMap
import com.wx.base.dialog.DialogHelp
import com.wx.base.ext.i18N
import com.wx.base.util.Constant
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.R
import com.wx.detalk.adapter.FollowAdapter
import com.wx.detalk.databinding.FragmentFollowBinding
import com.wx.detalk.mvvm.viewmodel.mine.FollowFragmentViewModel
import i18n

/**
 * Created by huy  on 2023/6/21.
 */
class FollowFragment : VbBaseFragment<FollowFragmentViewModel, FragmentFollowBinding>(), OnClickListener {
    var followList: MutableList<Int> = ArrayList()
    var followAdapter: FollowAdapter? = null
    var followType = Constant.FRIENDS

    override fun initView() {
        followType = arguments?.getInt("followType", Constant.FRIENDS)!!

        binding.rvFollow.setHasFixedSize(true)
        binding.rvFollow.layoutManager = LinearLayoutManager(BaseApplication.instance())
        followAdapter = FollowAdapter(followList, followType, this)
        binding.rvFollow.adapter = followAdapter
    }

    override fun initListener() {
    }

    override fun initData() {

        followList.add(1)
        followList.add(1)
        followList.add(1)
        followList.add(1)
        followList.add(1)
        followList.add(1)
        followList.add(1)
        followAdapter?.setList(followList)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_followStatus -> {
                val followStatusView = v as TextView
                var follow = v.tag as Int
                DialogHelp.Builder().setTitle(i18N(com.wx.base.R.string.Tips))
                    .setContentMsg(i18N(com.wx.base.R.string.are_you_sure_to_unfollow))
                    .setLeftMsg(i18N(com.wx.base.R.string.Cancel))
                    .setRightMsg(i18N(com.wx.base.R.string.Confirm))
                    .setOnButtonClickLinsener(object : DialogHelp.OnButtonClickLinsener {
                        override fun onLeftClickLinsener(msg: String) {
                        }

                        override fun onRightClickLinsener(msg: String) {
                            if (follow == 1) {
                                followStatusView.background = ContextCompat.getDrawable(
                                    BaseApplication.instance(),
                                    com.wx.base.R.drawable.shape_yellow_bg
                                )
                                followStatusView.setTextColor(
                                    ContextCompat.getColor(
                                        BaseApplication.instance(),
                                        com.wx.base.R.color.black
                                    )
                                )
                                followStatusView.text = i18n(com.wx.base.R.string.Follow)
                            } else {
                                followStatusView.background = ContextCompat.getDrawable(
                                    BaseApplication.instance(),
                                    com.wx.common.R.drawable.shape_black_bg
                                )
                                followStatusView.setTextColor(
                                    ContextCompat.getColor(
                                        BaseApplication.instance(),
                                        com.wx.base.R.color.white_40_color
                                    )
                                )
                            }
                        }
                    })
                    .build(requireActivity()).showDailog()
            }

            R.id.rrl_follow -> {
                ARouter.getInstance().build(ARouterMap.PERSON_DETAIL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentFollowBinding.inflate(layoutInflater)
    }
}