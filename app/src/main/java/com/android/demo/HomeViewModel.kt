package com.android.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.demo.callbacks.BaseJsonViewModelListener
import com.android.demo.data.network.MainRepository
import com.android.demo.data.network.OnApiResponseListener
import com.google.gson.JsonElement

class HomeViewModel(var repository: MainRepository) : ViewModel() {

    var loading = MutableLiveData<Boolean>(false)

    // get order detail from list
    fun getPosts(listener: BaseJsonViewModelListener?) {
        loading.value = true
        repository.getPosts(object : OnApiResponseListener<JsonElement> {
            override fun onResponseComplete(response: JsonElement?, requestCode: Int) {
                loading.value = false
                listener?.onComplete(response)
            }

            override fun onResponseError(errorMessage: String?, requestCode: Int, responseCode: Int) {
                loading.value = false
                listener?.onError(errorMessage)
            }
        })
    }

    fun getComments(id: Int, listener: BaseJsonViewModelListener? = null) {
        loading.value = true
        repository.getComments(id,object : OnApiResponseListener<JsonElement> {
            override fun onResponseComplete(response: JsonElement?, requestCode: Int) {
                loading.value = false
                listener?.onComplete(response)
            }

            override fun onResponseError(errorMessage: String?, requestCode: Int, responseCode: Int) {
                loading.value = false
                listener?.onError(errorMessage)
            }
        })
    }

}
