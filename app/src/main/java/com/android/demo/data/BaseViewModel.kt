package com.android.demo.data

import androidx.lifecycle.ViewModel

open class BaseViewModel(val prefs: BaseRepository) : ViewModel() {

    fun getPrefs(key: PrefsKey): String = prefs.get(key)
    fun setPrefs(key: PrefsKey, value: String) = prefs.set(key, value)

    fun <T> getPrefs(key: PrefsKey, classOfT: Class<T>): T? = prefs.get(key, classOfT)
    fun setPrefs(key: PrefsKey, classOfT: Any) = prefs.set(key, classOfT)

    fun getPrefs(key: String): String = prefs.get(key)
    fun setPrefs(key: String, value: String) = prefs.set(key, value)

}