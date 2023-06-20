package com.wx.detalk.adapter.wallet

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.aries.ui.view.radius.RadiusFrameLayout
import com.aries.ui.view.radius.RadiusTextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wx.base.BaseApplication
import com.wx.base.ext.gone
import com.wx.base.ext.show
import com.wx.common.bean.KeyboardNumberBean
import com.wx.detalk.R

/**
 * Created by huy  on 2022/8/23.
 */
class KeyboardAdapter(data: MutableList<MultiItemEntity>?, val onClickListener: View.OnClickListener) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
    companion object {
        //数字
        val KEYBOARD_NUMBER = 0

        //删除
        val KEYBOARD_DELETE = 1
    }

    init {
        addItemType(KEYBOARD_NUMBER, R.layout.item_keyboard_number)
        addItemType(KEYBOARD_DELETE, R.layout.item_keyboard_delete)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        when (holder.itemViewType) {
            KEYBOARD_NUMBER -> {
                var keyboardView = holder.getView<RadiusTextView>(R.id.rtv_number)
                val keyboardNumberBean = item as KeyboardNumberBean
                holder.setText(R.id.rtv_number, keyboardNumberBean.number)
                if (keyboardNumberBean.number.isEmpty()) {
                    keyboardView.gone()
                } else {
                    keyboardView.show()
                }
                keyboardView.tag = keyboardNumberBean
                keyboardView.setOnClickListener(onClickListener)
                keyboardView.setOnTouchListener { v, event ->
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            keyboardView.delegate.setBackgroundColor(
                                ContextCompat.getColor(
                                    BaseApplication.instance(),
                                    com.wx.base.R.color.color_ffffee00
                                )
                            )
                        }
                        MotionEvent.ACTION_UP -> {
                            keyboardView.delegate.setBackgroundColor(
                                ContextCompat.getColor(
                                    BaseApplication.instance(),
                                    com.wx.base.R.color.color_ff292929
                                )
                            )
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            keyboardView.delegate.setBackgroundColor(
                                ContextCompat.getColor(
                                    BaseApplication.instance(),
                                    com.wx.base.R.color.color_ff292929
                                )
                            )
                        }
                    }
                    keyboardView.delegate.init()
                    false
                }
            }
            KEYBOARD_DELETE -> {
                holder.getView<RadiusFrameLayout>(R.id.rfl_delete).setOnClickListener(onClickListener)
            }
        }
    }

}