package com.android.demo.base

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.util.Base64
import android.webkit.WebView
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.android.demo.data.CommonViewModelFactory
import com.android.demo.data.MyPrefs
import com.android.demo.data.db.AppDatabase
import com.android.demo.data.network.MainRepository
import com.android.demo.data.network.UserApi
import com.android.demo.util.AppSignatureHelper
import com.android.demo.util.logd
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.security.MessageDigest

class BaseApplication : MultiDexApplication(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserApi() }
        bind() from singleton { MyPrefs(instance()) }
        bind() from singleton { MainRepository(instance(), instance(), instance()) }
        bind() from provider { CommonViewModelFactory(instance()) }
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        generateKeyHash()?.logd()
        AppSignatureHelper(this).appSignatures.toString().logd()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getProcessName(this).let { if (packageName != it) WebView.setDataDirectorySuffix(it) }
        }
    }

    private fun generateKeyHash(): String? {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return String(Base64.encode(md.digest(), 0))
            }
        } catch (e: Exception) {
            ("exception: " + e.toString()).logd()
        }
        return "key hash not found"
    }

    private fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }
}