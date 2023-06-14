package com.wx.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageView
import com.wx.common.util.GlideUtils
import java.lang.Exception

class ImageRadioView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context!!, attrs, defStyleAttr), Checkable {
    private var checked = false
    private val img = 0
    private var checkedImg = 0
    private var uncheckedImg = 0

    override fun isChecked(): Boolean {
        return checked
    }

    override fun setChecked(checked: Boolean) {
        try {
            this.checked = checked
            if (checked) {
                GlideUtils.instance?.loadImage(this, checkedImg)
            } else {
                GlideUtils.instance?.loadImage(this, uncheckedImg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setCheckedImg(checkedImg: Int) {
        this.checkedImg = checkedImg
    }

    fun setUnCheckedImg(uncheckedImg: Int) {
        this.uncheckedImg = uncheckedImg
    }

    override fun toggle() {
        isChecked = !checked
    }
}