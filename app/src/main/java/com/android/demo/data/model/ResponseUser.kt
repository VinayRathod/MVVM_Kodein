package com.android.demo.data.model

class ResponseUser {
    var id: String? = null
    var name: String? = null
    var first_name: String? = null
    var last_name: String? = null
    var gender: String? = null
    var email: String? = null
    var image_url: String? = null
    var pincode: String? = null
    var phone: String? = null

    override fun toString(): String {
        return "ResponseUser(id=$id, name=$name, first_name=$first_name, last_name=$last_name, gender=$gender, email=$email, image_url=$image_url, pincode=$pincode, phone=$phone)"
    }
}
