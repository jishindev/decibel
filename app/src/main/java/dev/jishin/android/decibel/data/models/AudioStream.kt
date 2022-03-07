package dev.jishin.android.decibel.data.models

import android.media.AudioManager
import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioStream(val name: String, val value: Int): Parcelable {
    companion object {
        fun getAll() = mutableListOf(
            AudioStream("music", AudioManager.STREAM_MUSIC),
            AudioStream("alarm", AudioManager.STREAM_ALARM),
            AudioStream("dtmf", AudioManager.STREAM_DTMF),
            AudioStream("ring", AudioManager.STREAM_RING),
            AudioStream("system", AudioManager.STREAM_SYSTEM),
            AudioStream("notification", AudioManager.STREAM_NOTIFICATION),
            AudioStream("voice call", AudioManager.STREAM_VOICE_CALL),
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                add(AudioStream("music", AudioManager.STREAM_ACCESSIBILITY))
            }
        }.toList()
    }
}