package com.wx.detalk.ui.fragment

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.wx.base.viewbase.VbBaseFragment
import com.wx.detalk.R
import com.wx.detalk.adapter.DaoClubAdapter
import com.wx.detalk.databinding.FragmentDaoClubBinding
import com.wx.detalk.mvvm.viewmodel.community.DaoClubViewModel

/**
 * Created by huy  on 2023/6/16.
 */
class DaoClubFragment : VbBaseFragment<DaoClubViewModel, FragmentDaoClubBinding>(), OnClickListener {
    var daoClubList: MutableList<Int> = ArrayList()
    var daoClubAdapter: DaoClubAdapter? = null

    override fun initView() {
        binding.rvDaoClub.setHasFixedSize(true)
        binding.rvDaoClub.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        daoClubAdapter = DaoClubAdapter(daoClubList, this)
        binding.rvDaoClub.adapter = daoClubAdapter
    }

    override fun initListener() {
    }

    override fun initData() {
        daoClubList.add(1)
        daoClubList.add(1)
        daoClubList.add(1)
        daoClubList.add(1)
        daoClubList.add(1)
        daoClubList.add(1)
        daoClubList.add(1)
        daoClubAdapter?.setList(daoClubList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rll_daoClub -> {

            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentDaoClubBinding.inflate(layoutInflater)
    }

}