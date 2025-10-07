package com.example.produtosdelimpeza.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.produtosdelimpeza.model.CartProduct

@Database(entities = [CartProduct::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartProductsDao(): CartProductsDAO

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val DATABASE_NAME = "cart_products_database"

        fun getInstance(context: Context): AppDatabase {
            // Se a INSTANCE já existe, retorna a existente (otimização de performance)
            return INSTANCE ?: synchronized(this) {
                // Se ainda for nula após o bloqueio, cria o banco
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