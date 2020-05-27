package com.android.demo.data.network

import com.android.demo.data.BaseRepository
import com.android.demo.data.MyPrefs
import com.android.demo.data.db.AppDatabase
import com.google.gson.JsonElement
import retrofit2.Call

class MainRepository(private val api: UserApi, private val db: AppDatabase, val prefs: MyPrefs) : BaseRepository(prefs) {

    fun getPosts(listener: OnApiResponseListener<JsonElement>) {
        val call: Call<JsonElement> = api.getPosts()
        call.enqueue(APICallBack(listener, ApiCode.GET_CROPS))
    }

    fun getComments(id: Int, listener: OnApiResponseListener<JsonElement>) {
        val call: Call<JsonElement> = api.getComments(id)
        call.enqueue(APICallBack(listener, ApiCode.GET_CROPS))
    }

    fun reset() {
        try {
            prefs.reset()
            db.getProductDao().deleteAllBrand()
            db.getProductDao().deleteAllCategory()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}