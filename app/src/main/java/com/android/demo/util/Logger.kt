package com.android.demo.util

import android.content.Context
import android.os.Bundle
import com.android.demo.BuildConfig
import org.json.JSONObject


val Debug = BuildConfig.DEBUG

class Logger {
    companion object {
        var ctx: Context? = null
        fun analytics(event: String?, vararg params: String?): Bundle {
            val bundle = Bundle()
            if (!Debug) {
                val args = JSONObject()
                var i = 0
                try {
                    while (i < params.size) {
                        if (i + 1 < params.size) {
                            bundle.putString(params[i], params[i + 1])
                            args.put(params[i], params[i + 1])
                        }
                        i += 2
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (event != null) {
                    "$event -> $bundle".logd()
//                    FirebaseCrashlytics.getInstance().log("$event -> $bundle")
//                    ctx?.let { FirebaseAnalytics.getInstance(it).logEvent(event, bundle) }
                }
            }
            return bundle
        }

        fun analytics(ctx: Context, event: String?, vararg params: String?) {
            try {
                if (event != null && !Debug) {
                    Logger.ctx = ctx
//                    val logger = AppEventsLogger.newLogger(ctx)
//                    logger.logEvent(event, analytics(event, *params))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

