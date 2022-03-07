package dev.jishin.android.decibel.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.provider.Settings
import android.view.accessibility.AccessibilityManager


inline fun <reified T : AccessibilityService> Context.isAccessibilityServiceEnabled(): Boolean {
    val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices: List<AccessibilityServiceInfo> =
        am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
    for (enabledService in enabledServices) {
        val enabledServiceInfo = enabledService.resolveInfo.serviceInfo
        if (enabledServiceInfo.packageName.equals(packageName) &&
            enabledServiceInfo.name.equals(T::class.java.name)
        ) return true
    }
    return false
}

fun Context.showVolumeSliders() {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    audioManager!!.setStreamVolume(
        AudioManager.STREAM_MUSIC,
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
        AudioManager.FLAG_SHOW_UI// or AudioManager.FLAG_VIBRATE or AudioManager.USE_DEFAULT_STREAM_TYPE
    )
}

fun Context.openAccessibilitySettings() {
    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    })
}