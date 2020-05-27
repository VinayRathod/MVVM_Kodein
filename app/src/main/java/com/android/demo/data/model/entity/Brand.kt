package com.android.demo.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Brand(
    @PrimaryKey
    @ColumnInfo(name = "brand_id")
    val id: Int,
    var title: String? = "",
    var image: String? = "",
    var description: String? = "",
    var brand_logo: String? = "",
    var brand_filter_image: String? = ""
)