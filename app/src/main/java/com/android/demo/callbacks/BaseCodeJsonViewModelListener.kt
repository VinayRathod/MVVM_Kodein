package com.android.demo.callbacks

import com.google.gson.JsonElement

interface BaseCodeJsonViewModelListener {
    fun onComplete(json: JsonElement? = null, message: String? = null)
    fun onError(code: Int?, message: String?)
}