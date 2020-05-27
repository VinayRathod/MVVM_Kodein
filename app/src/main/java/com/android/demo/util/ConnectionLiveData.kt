package com.android.demo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData

class ConnectionLiveData(private val context: Context) : LiveData<Boolean?>() {
    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                intent.extras?.let {
                    postValue((it[ConnectivityManager.EXTRA_NETWORK_INFO] as NetworkInfo).isConnectedOrConnecting)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

}