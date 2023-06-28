package com.wx.detalk.ui.fragment

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.wx.base.BaseApplication
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.adapter.MineReplyAdapter
import com.wx.detalk.databinding.FragmentMineReplyBinding
import com.wx.detalk.mvvm.viewmodel.mine.MineReplyViewModel

/**
 * Created by huy  on 2023/6/26.
 */
class MineReplyFragment : VbBaseFragment<MineReplyViewModel, FragmentMineReplyBinding>(), OnClickListener {
    var mineReplyAdapter: MineReplyAdapter? = null
    var mineReplyList: MutableList<Int> = ArrayList()
    override fun initView() {
        binding.rvReply.setHasFixedSize(true)
        binding.rvReply.layoutManager = LinearLayoutManager(BaseApplication.instance())
        mineReplyAdapter = MineReplyAdapter(mineReplyList, this)
        binding.rvReply.adapter = mineReplyAdapter
    }

    override fun initData() {
        mineReplyList.add(0)
        mineReplyList.add(1)
        mineReplyList.add(2)
        mineReplyList.add(3)
        mineReplyList.add(4)
        mineReplyList.add(5)
        mineReplyList.add(6)
        mineReplyList.add(7)
        mineReplyAdapter?.setList(mineReplyList)
    }

    override fun initListener() {
        binding.srlPost.setOnRefreshListener {
            binding.srlPost.finishRefresh(5000)
        }
        binding.srlPost.setOnLoadMoreListener {
            binding.srlPost.finishLoadMore(5000)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentMineReplyBinding.inflate(layoutInflater)
    }
}