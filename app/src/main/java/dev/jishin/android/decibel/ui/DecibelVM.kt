package dev.jishin.android.decibel.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import dev.jishin.android.decibel.App
import dev.jishin.android.decibel.DecibelService
import dev.jishin.android.decibel.data.repos.PrefsRepo
import dev.jishin.android.decibel.utils.isAccessibilityServiceEnabled
import kotlinx.coroutines.flow.MutableStateFlow

class DecibelVM(app: Application) : AndroidViewModel(app) {

    val decibelEnabledFlow = MutableStateFlow(false)
    private val prefsRepo by lazy { PrefsRepo(app) }

    fun getAudioStreamFlow() = prefsRepo.audioStreamFlow

    fun refreshDecibelEnabledState() {

        val isEnabled = getApplication<App>().isAccessibilityServiceEnabled<DecibelService>()

        Log.d(TAG, "refreshDecibelEnabledState: isEnabled: $isEnabled")

        decibelEnabledFlow.tryEmit(isEnabled)
    }

    companion object {
        private const val TAG = "DecibelVM"
    }
}