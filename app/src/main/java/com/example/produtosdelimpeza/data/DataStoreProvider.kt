package com.example.produtosdelimpeza.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


// 1. Nome para as configurações (o que já tínhamos)
private const val NAVIGATION_PREFERENCES_NAME = "profile_mode"


// 3. Extensão para as Configurações
val Context.navigationDataStore: DataStore<Preferences> by preferencesDataStore(name = NAVIGATION_PREFERENCES_NAME)
