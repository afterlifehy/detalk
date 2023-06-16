package com.wx.detalk.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.wx.detalk.ui.activity.MyCaptureActivity

/**
 * Created by huy  on 2023/6/15.
 */
class ScanResultContract : ActivityResultContract<String, String>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, MyCaptureActivity::class.java).apply {
            putExtra("input", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val result = intent?.getStringExtra("result")
        return if (resultCode == Activity.RESULT_OK && result != null) result
        else null
    }
}