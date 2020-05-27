package com.android.demo.callbacks

import android.view.View

interface OnListItemClickListener {
    fun onItemClick(view: View, position: Int, id: Int? = null, extra: String? = null)
}