package com.android.demo.callbacks

import com.google.gson.JsonElement

interface BaseJsonViewModelListener {
    fun onComplete(json: JsonElement?)
    fun onError(message: String?)
}