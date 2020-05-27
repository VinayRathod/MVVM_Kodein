package com.android.demo.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?, var name: String? = "", var full_image: String? = ""
)