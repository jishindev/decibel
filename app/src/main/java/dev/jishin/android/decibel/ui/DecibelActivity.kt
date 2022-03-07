package dev.jishin.android.decibel.ui

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import dev.jishin.android.decibel.ui.theme.DecibelTheme


class DecibelActivity : ComponentActivity() {

    private val decibelVM by viewModels<DecibelVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DecibelTheme {

                val decibelEnabledState = decibelVM.decibelEnabledFlow.collectAsState()
                SettingsScreen(decibelEnabledState.value)

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    // Create an observer that triggers our remembered callbacks
                    val lifecycleEventObserver = LifecycleEventObserver { _, event ->


                        val accessibilityObserver = object : ContentObserver(Handler(mainLooper)) {
                            override fun onChange(selfChange: Boolean) {
                                super.onChange(selfChange)
                                decibelVM.refreshDecibelEnabledState()
                            }
                        }

                        when (event) {
                            Lifecycle.Event.ON_START -> {
                                decibelVM.refreshDecibelEnabledState()
                                val uri =
                                    Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                                contentResolver.registerContentObserver(
                                    uri,
                                    false,
                                    accessibilityObserver
                                )
                            }
                            Lifecycle.Event.ON_STOP -> {
                                contentResolver.unregisterContentObserver(accessibilityObserver)
                            }
                            else -> {}
                        }
                    }

                    // Add the observer to the lifecycle
                    lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

                    // When the effect leaves the Composition, remove the observer
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
                    }
                }
            }
        }
    }
}