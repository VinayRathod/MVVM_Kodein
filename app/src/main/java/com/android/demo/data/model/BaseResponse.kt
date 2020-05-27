package com.android.demo.data.model

import com.google.gson.JsonElement

open class BaseResponse {
    var status: JsonElement? = null
    var message: String? = null
    var data: JsonElement? = null

    fun isSuccess(): Boolean {
        try {
            return status?.asInt == 200
        } catch (e: Exception) {
            e.printStackTrace()
            return status?.asString?.equals("ok") == true
        }
    }
}