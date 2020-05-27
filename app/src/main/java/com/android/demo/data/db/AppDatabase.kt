package com.android.demo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.demo.data.model.entity.Brand
import com.android.demo.data.model.entity.Category

@Database(
    entities = [Category::class, Brand::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "MyDatabase.db"
        ).build()
    }
}