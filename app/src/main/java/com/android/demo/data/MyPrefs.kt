package com.android.demo.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.demo.util.logd
import com.google.gson.Gson

class MyPrefs(var context: Context) {

    fun myContext(): Context {
        return context
    }

    private val myPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    //  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - String - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    operator fun get(key: String): String {
        return myPrefs.getString(key, "") ?: ""
    }

    operator fun get(key: PrefsKey): String {
        return myPrefs.getString(key.getName(), "") ?: ""
    }

    operator fun set(key: String, value: String) {
        ("Adding Prefs : " + key + " > " + value).logd()
        val prefEditor = myPrefs.edit()
        prefEditor.putString(key, value)
        prefEditor.apply()
    }

    operator fun set(key: String, value: Int) {
        ("Adding Prefs : " + key + " > " + value).logd()
        val prefEditor = myPrefs.edit()
        prefEditor.putInt(key, value)
        prefEditor.apply()
    }

    operator fun set(key: String, value: MutableSet<String>) {
        ("Adding Prefs : " + key + " > " + value).logd()
        val prefEditor = myPrefs.edit()
        prefEditor.putStringSet(key, value)
        prefEditor.apply()
    }

    fun getSet(key: String): MutableSet<String> {
        val set = HashSet<String>()
        return myPrefs.getStringSet(key, set) ?: set
    }

    operator fun set(key: PrefsKey, value: String) {
        set(key.getName(), value)
    }

    //  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Int - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    fun getInt(key: String): Int {
        try {
            myPrefs.getInt(key, 0)
        } catch (e: Exception) {
            try {
                val value = myPrefs.getString(key, "0")
                val prefEditor = myPrefs.edit()
                prefEditor.remove(key)
                prefEditor.apply()
                set(key, value?.toInt() ?: 0)
            } catch (e: Exception) {
                set(key, 0)
            }
        }
        return myPrefs.getInt(key, 0)
    }

    fun getInt(key: PrefsKey): Int {
        try {
            myPrefs.getInt(key.getName(), 0)
        } catch (e: Exception) {
            try {
                val value = myPrefs.getString(key.getName(), "0")
                val prefEditor = myPrefs.edit()
                prefEditor.remove(key.getName())
                prefEditor.apply()
                set(key, value?.toInt() ?: 0)
            } catch (e: Exception) {
                set(key.getName(), 0)
            }
        }
        return myPrefs.getInt(key.getName(), 0)
    }

    fun getLong(key: PrefsKey): Long {
        return myPrefs.getLong(key.getName(), 0)
    }

    operator fun set(key: PrefsKey, value: Long) {
        ("Adding Prefs : " + key.getName() + " > " + value).logd()
        val prefEditor = myPrefs.edit()
        prefEditor.putLong(key.getName(), value)
        prefEditor.apply()
    }

    operator fun set(key: PrefsKey, value: Int) {
        ("Adding Prefs : " + key.getName() + " > " + value).logd()
        val prefEditor = myPrefs.edit()
        prefEditor.putInt(key.getName(), value)
        prefEditor.apply()
    }

    //  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Boolean - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    fun getBoolean(key: PrefsKey): Boolean {
        return myPrefs.getBoolean(key.getName(), false)
    }

    operator fun set(key: PrefsKey, value: Boolean) {
        ("Adding Prefs : " + key.getName() + " > " + value).logd()
        val prefEditor = myPrefs.edit()
        prefEditor.putBoolean(key.getName(), value)
        prefEditor.apply()
    }


    //  - - - - - - - - - - - - - - - - - - l- - - - - - - - - - - - - - - - - - - - - - - - - - Pojo - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    operator fun <T> get(key: PrefsKey, classOfT: Class<T>): T? {
        return Gson().fromJson(get(key), classOfT)
    }

    operator fun set(key: PrefsKey, classOfT: Any): Boolean {
        val data = Gson().toJson(classOfT)
        if (get(key) == data) return false
        else {
            set(key, data)
            return true
        }
    }

    fun clearAll() {
//        API.setToken("", "")
        myPrefs.edit().clear().apply()
    }

    fun reset() {
        clearAll()
    }

    companion object {
        private val LOCK = Any()
        private var instance: MyPrefs? = null
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: MyPrefs(context).also {
                instance = it
            }
        }
    }
}
