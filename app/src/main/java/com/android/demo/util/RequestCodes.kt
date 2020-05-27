package com.android.demo.util

import android.content.Context
import com.android.demo.MainActivity

fun Context.loading(isLoad: Boolean) {
    if (this is MainActivity) loading(isLoad)
    else if (this is BaseActivity) loading(isLoad)
}

val BaseActivity.Companion.CODE by lazy { 1001 }
