package com.wx.base.http.interceptor

import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.EncryptUtils
import com.wx.base.util.Constant
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var sortParam = ""

        var request = chain.request()
        val addHeader = request.newBuilder()
        runBlocking {
            addHeader.addHeader("Content-Type", "application/json")
                .addHeader("os", "android")
                .addHeader("version", AppUtils.getAppVersionName())
                .addHeader("Authorization", "token")
//                .addHeader("sign", EncryptUtils.encryptMD5ToString(sortParam + timeStamp + Constant.secret))
        }

        val timeStamp = 1687143312779//System.currentTimeMillis().toString()
        request = addIntoBody(request, "timestamp", timeStamp)

        val body = request.body
        val buffer = Buffer()
        body?.writeTo(buffer)
        var charset = Charset.forName("UTF-8")
        val contentType = body?.contentType()
        if (contentType == null) {
            sortParam = ""
        } else if (TextUtils.equals(contentType.type + contentType.subtype, "applicationx-www-form-urlencoded")) {
            charset = contentType.charset(charset)
            val requestParams = buffer.readString(charset)
            sortParam = getSortForm(requestParams).toString()
        } else {
            charset = contentType.charset(charset)
            val requestParams = buffer.readString(charset)
            if (TextUtils.isEmpty(requestParams)) {//防止参数为空的时候，导致闪退
                sortParam = requestParams
            } else {
                sortParam = getSortJson(JSONObject(requestParams)).toString()
            }
        }

        request = addIntoBody(request, "sign", EncryptUtils.encryptMD5ToString(sortParam + Constant.secret))
        return chain.proceed(request)
    }

    fun addIntoBody(request: Request, key: String, value: Any): Request {
        val body = request.body
        val buffer = Buffer()
        body?.writeTo(buffer)
        var params = buffer.readUtf8()
        val paramsObject = com.alibaba.fastjson.JSONObject.parseObject(params)
        paramsObject[key] = value
        val requestBody =
            paramsObject.toJSONString().toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
        var newRequest = request.newBuilder().post(requestBody).build()
        return newRequest
    }

    fun getSortJson(json: JSONObject): String {
        val iteratorKeys: Iterator<String> = json.keys().iterator()
        val map: TreeMap<String?, String?> = TreeMap()
        while (iteratorKeys.hasNext()) {
            val key = iteratorKeys.next()
            val value = json.getString(key)
            map[key] = value
        }
        var sort = ""
        val keySets: List<String> = ArrayList<String>(map.keys)
        for (i in 0 until map.size - 1) {
            val key = keySets[i]
            val value = map[key].toString()
            sort += URLEncoder.encode(key, Charsets.UTF_8.name()) + "=" + URLEncoder.encode(
                value,
                Charsets.UTF_8.name()
            ) + "&"
        }
        val key = keySets[map.size - 1]
        val value = map[key].toString()
        sort += URLEncoder.encode(key, Charsets.UTF_8.name()) + "=" + URLEncoder.encode(
            value,
            Charsets.UTF_8.name()
        )
        return sort
    }

    fun getSortForm(form: String): String? {
        val list = ArrayList<String>()
        if (form.contains("&")) {
            val temp = form.split("&")
            list.addAll(temp)
        } else {
            list.add(form)
        }
        Collections.sort(list)
        var sort = ""
        for (i in list.indices) {
            list[i].replace("=", "")
            sort += list[i]
        }
        return sort
    }
}
