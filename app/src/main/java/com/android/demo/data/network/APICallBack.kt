package com.android.demo.data.network

import android.net.Uri
import com.android.demo.data.model.BaseResponse
import com.android.demo.util.logd
import com.android.demo.util.loge
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class APICallBack<T>(private val listener: OnApiResponseListener<T>?, private val requestCode: ApiCode) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (isSuccess(response)) {
            try {
                if (response.body() == null) {
                    ("posting-> (${requestCode.code}) ${requestCode.name}: " + response.code() + response.headers()).logd()
                    listener?.onResponseComplete(Integer.valueOf(response.code()) as T, requestCode.code)
                } else {
                    ("posting-> (${requestCode.code}) ${requestCode.name}: " + response.body().toString()).logd()
                    if (response.body() is BaseResponse) {
                        val resp = response.body() as BaseResponse
                        if (resp.isSuccess())
                            listener?.onResponseComplete(response.body(), requestCode.code)
                        else {
                            listener?.onResponseError(resp.message, requestCode.code, response.code())
                        }
                    } else if (response.body() is JsonElement && response.body() is JsonObject) {
                        val json = response.body() as JsonElement?
                        try {
                            if (json == null)
                                listener?.onResponseComplete(response.body(), requestCode.code)
                            else {
                                json.asJsonObject["status"]?.asString?.toInt()
                                if (json.asJsonObject["status"].asInt == 1)
                                    listener?.onResponseComplete(response.body(), requestCode.code)
                                else
                                    listener?.onResponseError(json.asJsonObject["message"].asString, requestCode.code, response.code())
                            }
                        } catch (e: Exception) {
                            listener?.onResponseComplete(response.body(), requestCode.code)
                        }
                    } else listener?.onResponseComplete(response.body(), requestCode.code)
                }
            } catch (e: Exception) {
                "posting-> try catch (${requestCode.code}) ${requestCode.name}: ".loge()
                try {
                    listener?.onResponseError(e.localizedMessage, requestCode.code, response.code())
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
                e.printStackTrace()
            }
        } else {
            "isSuccess false (${requestCode.code}) ${requestCode.name}: ".loge()
        }
    }

    private fun isHostOffline(msg: String?): Boolean {
        val url = Uri.parse(Api.baseUrl)
        return msg?.contains("Failed to connect to " + url.host) == true
    }

    companion object {
        val SERVER_ERROR = "Something went wrong. Please try later."
        val INTERNET_ERROR = "Internet Connection seems to be offline"
        var hasConnection = false
    }

    override fun onFailure(call: Call<T>, throwable: Throwable) {
        ("onFailure-> (${requestCode.code}) ${requestCode.name}: " + throwable.message).loge()
        try {
            if (!hasConnection)
                listener?.onResponseError(INTERNET_ERROR, requestCode.code, 0)
            else if (throwable is JsonSyntaxException) {
                listener?.onResponseError("Server Response Changed : " + throwable.message, requestCode.code, 0)
            } else if (throwable is MalformedJsonException) {
                listener?.onResponseError("some character are malformed in JSON : " + throwable.message, requestCode.code, 0)
            } else if (throwable is IllegalStateException) {
                listener?.onResponseError("" + throwable.message, requestCode.code, 0)
            } else if (throwable is SocketTimeoutException) {
                listener?.onResponseError("Server Time out. Please try again.", requestCode.code, 0)
            } else if (throwable is UnknownHostException || throwable is ConnectException) {
                listener?.onResponseError("Server down. Please try again.", requestCode.code, 0)
            } else if (throwable.message != null && throwable.message?.contains("No address associated with hostname") == true) {
                listener?.onResponseError(INTERNET_ERROR, requestCode.code, 0)
            } else if (throwable is ConnectException && throwable.message != null && isHostOffline(throwable.message)) {
                listener?.onResponseError(INTERNET_ERROR, requestCode.code, 0)
            } else {
                listener?.onResponseError("Exception : " + throwable.message, requestCode.code, -1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isSuccess(response: Response<T>): Boolean {
        if (!response.isSuccessful) {
            try {
                try {
                    val i = response.errorBody()?.byteStream()
                    val r = BufferedReader(InputStreamReader(i))
                    var errorResult = StringBuilder()
                    var line: String?
                    try {
                        while (r.readLine().also { line = it } != null) {
                            errorResult.append(line).append('\n')
                        }
                        val obj = Gson().fromJson(errorResult.toString(), JsonObject::class.java)
                        if (obj["Error"] != null) {
                            if (obj["Error"] is JsonObject) {
                                val error = obj["Error"].asJsonObject["status"].asString
                                errorResult = StringBuilder()
                                errorResult.append(error)
                            } else {
                                val error = obj["Error"].asString
                                errorResult = StringBuilder()
                                errorResult.append(error)
                            }
                        } else if (obj["message"] != null) {
                            val error = obj["message"].asString
                            errorResult = StringBuilder()
                            errorResult.append(error)
                        }
                        val error = obj["Error"].asString + "\n" + obj["message"].asString
                        errorResult.append(error)
                        errorResult = StringBuilder()
                        errorResult.append(error)
                    } catch (e: Exception) {
                    }
                    val errormsg = errorResult.toString()
                    ("response (${requestCode.code}) ${requestCode.name}: $errormsg").loge()
                    listener?.onResponseError(errormsg, requestCode.code, response.code())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
        return true
    }
}