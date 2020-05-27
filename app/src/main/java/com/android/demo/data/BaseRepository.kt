package com.android.demo.data

import com.android.demo.data.model.ResponseUser

abstract class BaseRepository(private val prefs: MyPrefs) {

    fun getInt(key: PrefsKey): Int = prefs.getInt(key)
    fun set(key: PrefsKey, value: Int) = prefs.set(key, value)

    fun get(key: PrefsKey): String = prefs.get(key)
    fun set(key: PrefsKey, value: String) = prefs.set(key, value)

    fun <T> get(key: PrefsKey, classOfT: Class<T>): T? = prefs.get(key, classOfT)
    fun set(key: PrefsKey, classOfT: Any) = prefs.set(key, classOfT)

    fun set(key: PrefsKey, time: Long) = prefs.set(key, time)

    fun get(key: String): String = prefs.get(key)
    fun set(key: String, value: String) = prefs.set(key, value)
    fun set(key: String, value: Int) = prefs.set(key, value)
    fun set(key: String, value: MutableSet<String>) = prefs.set(key, value)
    fun getInt(key: String): Int = prefs.getInt(key)
    fun getSet(key: String): MutableSet<String> = prefs.getSet(key)

    fun getProfile(): ResponseUser? {
        return prefs.get(PrefsKey.PROFILE, ResponseUser::class.java)
    }

    fun newHashMap(key: String, value: String): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.put(key, value)
        return map
    }

    fun newHashMap(vararg pairs: String?): HashMap<String, String> {
        val map = HashMap<String, String>()
        var i = 0
        while (i < pairs.size) {
            if (pairs[i] != null && pairs[i + 1] != null) map.put(pairs[i]!!, pairs[i + 1]!!)
            i += 2
        }
        return map
    }
}