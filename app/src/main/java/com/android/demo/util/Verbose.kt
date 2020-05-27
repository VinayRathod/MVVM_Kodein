package com.android.demo.util

import android.text.TextUtils
import android.util.Log
import com.android.demo.BuildConfig

val VERBOSE = Log.VERBOSE
val DEBUG = Log.DEBUG
val INFO = Log.INFO
val WARN = Log.WARN
val ERROR = Log.ERROR
val WTF = Log.ASSERT

private val TAG = "LogHelper"

fun log(level: Int, msg: String?, throwable: Throwable?) {
    if (BuildConfig.DEBUG) {
        val elements = Throwable().stackTrace
        var callerClassName = "?"
        var callerMethodName = "?"
        var callerLineNumber = "?"
        if (elements.size >= 4) {
            callerClassName = elements[3].className
            callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
            if (callerClassName.indexOf("$") > 0) {
                callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"))
            }
            callerMethodName = elements[3].methodName
            callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1)
            if (callerMethodName == "<init>") {
                callerMethodName = callerClassName
            }
            callerLineNumber = elements[3].lineNumber.toString()
        }

        val stack =
            "[" + callerClassName + "." + callerMethodName + "():" + callerLineNumber + "]" + if (TextUtils.isEmpty(
                    msg
                )
            ) "" else " "

        when (level) {
            VERBOSE -> Log.v(TAG, stack + msg, throwable)
            DEBUG -> Log.d(TAG, stack + msg, throwable)
            INFO -> Log.i(TAG, stack + msg, throwable)
            WARN -> Log.w(TAG, stack + msg, throwable)
            ERROR -> Log.e(TAG, stack + msg, throwable)
            WTF -> Log.wtf(TAG, stack + msg, throwable)
            else -> {
            }
        }
    }
}

fun Any.logd(tag: String = "TAG", throwable: Throwable? = null) {
    log(DEBUG, this.toString(), throwable)
}

fun Any.loge(tag: String = "", throwable: Throwable? = null) {
    log(ERROR, this.toString(), throwable)
}

fun Any.logv(tag: String = "", throwable: Throwable? = null) {
    log(VERBOSE, this.toString(), throwable)
}

fun Any.logw(tag: String = "", throwable: Throwable? = null) {
    log(WARN, this.toString(), throwable)
}

fun Any.logwtf(tag: String = "", throwable: Throwable? = null) {
    log(WTF, this.toString(), throwable)
}

fun Any.loi(tag: String = "", throwable: Throwable? = null) {
    log(INFO, this.toString(), throwable)
}