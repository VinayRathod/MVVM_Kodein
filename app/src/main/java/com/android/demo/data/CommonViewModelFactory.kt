package com.android.demo.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.demo.HomeViewModel
import com.android.demo.data.network.MainRepository

@Suppress("UNCHECKED_CAST")
class CommonViewModelFactory(val repository: MainRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (modelClass) {
            HomeViewModel::class.java -> return HomeViewModel(repository) as T
            else -> return modelClass.getConstructor().newInstance()
        }
    }
}