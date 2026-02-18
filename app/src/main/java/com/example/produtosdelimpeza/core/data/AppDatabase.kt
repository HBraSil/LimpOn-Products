package com.example.produtosdelimpeza.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.produtosdelimpeza.core.data.dao.UserDao
import com.example.produtosdelimpeza.core.data.local.Converters
import com.example.produtosdelimpeza.customer.cart.data.CartProductsDAO
import com.example.produtosdelimpeza.core.data.entity.ProductEntity
import com.example.produtosdelimpeza.core.data.entity.StoreEntity
import com.example.produtosdelimpeza.core.data.entity.UserEntity
import com.example.produtosdelimpeza.store.onboarding.data.StoreDao

@TypeConverters(Converters::class)
@Database(
    entities = [ProductEntity::class, UserEntity::class, StoreEntity::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartProductsDao(): CartProductsDAO
    abstract fun userDao(): UserDao
    abstract fun storeDao(): StoreDao


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val DATABASE_NAME = "cart_products_database"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}