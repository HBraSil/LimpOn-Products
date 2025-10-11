package com.example.produtosdelimpeza

import android.app.Application
import com.example.produtosdelimpeza.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
    }
}