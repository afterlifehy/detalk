package com.wx.detalk.ui.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupWindow
import com.wx.detalk.R
import com.wx.detalk.databinding.PopCreateBoxBinding

/**
 * Created by huy  on 2023/6/15.
 */
class CreateBoxPop(val context: Context?, val callback: CreateBoxPopCallBack) : PopupWindow(context), OnClickListener {
    init {
        initView()
    }

    private fun initView() {
        val popCreateBoxBinding = PopCreateBoxBinding.inflate(LayoutInflater.from(context))
        popCreateBoxBinding.tvCreateDao.setOnClickListener(this)
        popCreateBoxBinding.tvCreateClub.setOnClickListener(this)
        contentView = popCreateBoxBinding.root
        contentView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        this.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.isOutsideTouchable = true
        val dw = ColorDrawable(-0)
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_createDao -> {
                callback.createDao()
            }

            R.id.tv_createClub -> {
                callback.createClub()
            }
        }
    }

    interface CreateBoxPopCallBack {
        fun createDao()
        fun createClub()
    }
}