package com.android.demo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.demo.data.model.entity.Brand
import com.android.demo.data.model.entity.Category

@Dao
interface ProductDao {

    @Query("DELETE FROM Brand")
    fun deleteAllBrand()

    @Query("DELETE FROM Category")
    fun deleteAllCategory()

    //----------------------------------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllCategories(category: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllBrands(brand: List<Brand>)

}