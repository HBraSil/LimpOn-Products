package com.example.produtosdelimpeza.customer.search.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SEARCH_HISTORY_KEY = "search_history"
val Context.searchScreenDataStore: DataStore<Preferences> by preferencesDataStore(name = "search_screen_prefs")

class SearchLocalDataStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore: DataStore<Preferences> = context.searchScreenDataStore

    val searcHistoryKey = stringSetPreferencesKey(SEARCH_HISTORY_KEY)

    suspend fun saveSearchHistory(history: Set<String>) {
        dataStore.edit {
            it[searcHistoryKey] = history
        }
    }

    suspend fun getSearchHistory(): Set<String> {
        return dataStore.data.map { preferences ->
            preferences[searcHistoryKey] ?: emptySet()
        }.first()
    }
}