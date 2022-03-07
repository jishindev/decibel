package dev.jishin.android.decibel.data.repos

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefsRepo(private val context: Context) {

    val audioStreamFlow: Flow<String?> = context.dataStore.data.map { it[KEY_AUDIO_STREAM] }

    suspend fun saveAudioStream(audioStream: String) {
        context.dataStore.edit { it[KEY_AUDIO_STREAM] = audioStream }
    }

    // to make sure there's only one instance
    companion object {
        private val Context.dataStore by preferencesDataStore("settings")
        val KEY_AUDIO_STREAM = stringPreferencesKey("key_audio_stream")
    }

}