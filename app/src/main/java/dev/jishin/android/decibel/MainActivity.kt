package dev.jishin.android.decibel

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.jishin.android.decibel.ui.theme.DecibelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DecibelTheme {
                MainUI()
            }
        }
    }
}

@Composable
fun MainUI() {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = { context.showVolumeSliders() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "volume")
            }
        }
    }
}

fun Context.showVolumeSliders() {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    audioManager!!.setStreamVolume(
        AudioManager.STREAM_MUSIC,
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
        AudioManager.FLAG_SHOW_UI// or AudioManager.FLAG_VIBRATE or AudioManager.USE_DEFAULT_STREAM_TYPE
    )
}

private fun Context.takeScreenshot() {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DecibelTheme {
        MainUI()
    }
}