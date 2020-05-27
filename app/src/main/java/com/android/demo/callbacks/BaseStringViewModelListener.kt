package com.android.demo.callbacks

interface BaseStringViewModelListener {
    fun onComplete(message: String?)
    fun onError(message: String?)
}