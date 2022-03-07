package dev.jishin.android.decibel.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jishin.android.decibel.R
import dev.jishin.android.decibel.data.models.AudioStream
import dev.jishin.android.decibel.ui.theme.DecibelTheme
import dev.jishin.android.decibel.utils.openAccessibilitySettings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    decibelEnabled: Boolean,
) {
    val context = LocalContext.current
    val showEnableDialog = rememberSaveable { mutableStateOf(false) }

    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {

                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_round),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.typography.displayLarge.color,
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (decibelEnabled.not()) {
                    Button(
                        onClick = { showEnableDialog.value = true },
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.End)
                    ) {
                        Text(text = stringResource(id = R.string.enable_service_title))
                    }
                } else {
                    AudioStreamDropDown()
                }
            }
        }
    }


    // show dialog to navigate to settings to enable decibel accessibility service
    EnableDecibelDialog(
        show = showEnableDialog.value,
        onDismiss = { showEnableDialog.value = false/*(context as? Activity?)?.finish()*/ },
        onConfirm = { showEnableDialog.value = false;context.openAccessibilitySettings() }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AudioStreamDropDown() {
    val audioStreams = AudioStream.getAll()
    val expanded = rememberSaveable { mutableStateOf(false) }
    val selectedAudioStream = rememberSaveable { mutableStateOf(audioStreams[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        },
    ) {
        OutlinedTextField(
            value = selectedAudioStream.value.name,
            onValueChange = { name: String ->
                selectedAudioStream.value =
                    audioStreams[audioStreams.indexOfFirst { it.name == name }]
            },
            readOnly = true,
            label = { Text(text = stringResource(R.string.audio_stream)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            audioStreams.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedAudioStream.value =
                            audioStreams[audioStreams.indexOfFirst { it.name == selectionOption.name }]
                        expanded.value = false
                    },
                    text = {
                        Text(text = selectionOption.name)
                    })
            }
        }
    }
}


@Composable
fun EnableDecibelDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Log.d(TAG, "EnableDecibelDialog() called with: show = $show")

    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) { Text(text = stringResource(id = R.string.go_to_settings)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text(text = stringResource(id = R.string.dismiss).lowercase()) }
            },
            title = { Text(text = stringResource(id = R.string.enable_service_title)) },
            text = { Text(text = stringResource(id = R.string.enable_service_desc)) })
    }
}

@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    DecibelTheme {
        SettingsScreen(false)
    }
}

@Preview(showBackground = true)
@Composable
fun EnableDecibelPreview() {
    EnableDecibelDialog(show = true, onDismiss = { /*TODO*/ }) {

    }
}

private const val TAG = "SettingsScreen"