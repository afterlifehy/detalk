package com.wx.detalk.ui.fragment

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.wx.base.BaseApplication
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.R
import com.wx.detalk.adapter.MinePostAdapter
import com.wx.detalk.databinding.FragmentMinePostBinding
import com.wx.detalk.mvvm.viewmodel.mine.MinePostViewModel

/**
 * Created by huy  on 2023/6/26.
 */
class MinePostFragment : VbBaseFragment<MinePostViewModel, FragmentMinePostBinding>(), OnClickListener {
    var minePostAdapter: MinePostAdapter? = null
    var minePostList: MutableList<Int> = ArrayList()
    override fun initView() {
        binding.rvPost.setHasFixedSize(true)
        binding.rvPost.layoutManager = LinearLayoutManager(BaseApplication.instance())
        minePostAdapter = MinePostAdapter(minePostList, this)
        binding.rvPost.adapter = minePostAdapter
    }

    override fun initData() {
        minePostList.add(0)
        minePostList.add(1)
        minePostList.add(2)
        minePostList.add(3)
        minePostList.add(4)
        minePostList.add(5)
        minePostList.add(6)
        minePostList.add(7)
        minePostAdapter?.setList(minePostList)
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
            R.id.riv_postImg -> {

            }

            R.id.cb_praise -> {

            }

            R.id.tv_comment -> {

            }

            R.id.cb_love -> {

            }

            R.id.iv_reward -> {

            }

            R.id.iv_share -> {

            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentMinePostBinding.inflate(layoutInflater)
    }
}