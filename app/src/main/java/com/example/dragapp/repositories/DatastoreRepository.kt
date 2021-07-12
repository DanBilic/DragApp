package com.example.dragapp.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


const val PREFERENCE_NAME = "app_preference"

class DatastoreRepository(context: Context) {

    private object PreferenceKeys{
        val token = preferencesKey<String>("token")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveToDataStore(token: String){
        dataStore.edit { preference ->
            preference[PreferenceKeys.token] = token
        }
    }

    val readFromDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map { preference ->
            val myToken = preference[PreferenceKeys.token] ?: "none"
            myToken
        }

}