package com.android.demo.data

enum class PrefsKey(private val data: String? = null) {
    ID,
    NUMBER,
    NAME,
    EMAIL,
    PROFILE_IMAGE,
    BIRTH_DATE,
    TOKEN,
    PIN_CODE,
    PROFILE,
    VERSION_NAME,
    DEVICEID;

    fun getName(): String {
        return data ?: name
    }

}