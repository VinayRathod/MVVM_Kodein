package com.android.demo.callbacks

interface BaseViewModelListener {
    fun onComplete()
    fun onError(message: String?)
}