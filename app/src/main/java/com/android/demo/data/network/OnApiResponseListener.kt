package com.android.demo.data.network

interface OnApiResponseListener<T> {
    /**
     * On response complete.
     *
     * @param clsGson     the cls gson
     * @param requestCode the request code
     */
    fun onResponseComplete(response: T?, requestCode: Int)

    /**
     * On response error.
     *
     * @param errorMessage the error message
     * @param requestCode  the request code
     */
    fun onResponseError(errorMessage: String?, requestCode: Int, responseCode: Int)
}