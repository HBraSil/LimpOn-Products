package com.example.produtosdelimpeza.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.commons.ProfileModeKey
import com.example.produtosdelimpeza.core.data.mapper.toProfileMode
import com.example.produtosdelimpeza.core.data.mapper.toProfileModeKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationLastUserModeRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val navigationDataStore = context.navigationDataStore

    companion object {
        val LAST_ACTIVE_PROFILE = stringPreferencesKey("last_active_profile")
    }

    val lastActiveProfile = navigationDataStore.data.map { preferences ->
        val storedValue = preferences[LAST_ACTIVE_PROFILE] ?: ProfileModeKey.LOGGED_OUT.name

        ProfileModeKey.valueOf(storedValue).toProfileMode()
    }

    suspend fun saveLastUserMode(profileMode: ProfileMode) {
        navigationDataStore.edit {
            it[LAST_ACTIVE_PROFILE] = profileMode.toProfileModeKey().name
        }
    }
}