package com.example.produtosdelimpeza

import android.app.Application
import com.example.produtosdelimpeza.core.data.AppDatabase
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS)


        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
        db = AppDatabase.getInstance(this)
    }
}