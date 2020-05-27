package com.android.demo.callbacks

interface BaseCodeViewModelListener {
    fun onComplete(message: String? = null)
    fun onError(code: Int?, message: String?)
}