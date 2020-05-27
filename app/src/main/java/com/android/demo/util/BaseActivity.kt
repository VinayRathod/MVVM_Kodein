package com.android.demo.util

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.demo.R
import com.android.demo.data.network.APICallBack
import java.lang.reflect.InvocationTargetException


abstract class BaseActivity : AppCompatActivity() {

    // showing progress dialog for loading data or calling api
    private var loaderDialog: Dialog? = null
    private var connectionDialog: Dialog? = null

    companion object {
        var loaderCount = 0
        var defaultTheme = ""
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            for (act in info.activities) {
                if (act.name.contains(javaClass.simpleName)) {
                    classname = javaClass.simpleName
                    break
                }
            }
//            val clazz: Class<*> = this::class.java
//            val method: Method = clazz.getMethod("getThemeResId")
//            method.setAccessible(true)
//            val themeResId = method.invoke(this)
//            if (defaultTheme.isEmpty()) {
//                defaultTheme = themeResId.toString()
//            }
//            if (defazultTheme != themeResId.toString())
//                attachSlider()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Logger.ctx = this
        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this, Observer { hasData ->
            APICallBack.hasConnection = hasData ?: false
            // findViewById<LinearLayout>(R.id._connection)?.let { it.visible(!hasData) }
//            if (this !is PreLoginActivity)
//                connection(!hasData)
        })
    }

    open fun loading(isShow: Boolean) {
        if (loaderDialog == null) {
            loaderDialog = Dialog(this, R.style.AppTheme)
            //            loaderDialog.setCancelable(false);
            loaderDialog?.setContentView(R.layout.loader_layout)
            loaderDialog?.window?.let {
                it.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                it.setBackgroundDrawable(ColorDrawable(0))
            }
            loaderDialog?.setOnCancelListener {
                loaderCount--
                "loading - $loaderCount".logd()
            }
        }

        if (isShow) {
            loaderCount++
            loaderDialog?.show()
        } else {
            loaderCount--
//            if (loaderCount < 1) {
//                loaderCount = 0
            loaderDialog?.dismiss()
//            }
        }
        "loading - $loaderCount".logd()
    }

    var activityVisible = false
    private var classname = ""
    override fun onResume() {
        super.onResume()
        activityVisible = true
        "onResume $classname".logd()
    }

    override fun onPause() {
        super.onPause()
        activityVisible = false
        "onPause $classname".logd()
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }
}
