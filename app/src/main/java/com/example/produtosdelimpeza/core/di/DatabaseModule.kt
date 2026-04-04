package com.example.produtosdelimpeza.core.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.data.AppDatabase
import com.example.produtosdelimpeza.core.data.dao.UserDao
import com.example.produtosdelimpeza.customer.cart.data.CartProductsDAO
import com.example.produtosdelimpeza.store.onboarding.data.StoreDao
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideCartProductsDAO(database: AppDatabase): CartProductsDAO {
        return database.cartProductsDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideStoreDao(database: AppDatabase): StoreDao {
        return database.storeDao()
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(
        @ApplicationContext context: Context,
    ): NetworkChecker = NetworkChecker(context)


    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("search_preferences")
            }
        )

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        Log.d("GOOGLE_API_KEY", "ALOHA")

        return LocationServices.getFusedLocationProviderClient(context)
    }
}