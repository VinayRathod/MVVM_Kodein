package com.android.demo.data.model

data class UserProfile(
    var firstname: String? = null, var lastname: String? = null, var facebookId: String? = null, var gmailId: String? = null,
    var mobile: String? = null, var email: String? = null, var imageUrl: String? = null, var pincode: String? = null,
    var isMobileValid: Boolean = false, var isEmailValid: Boolean = false
)