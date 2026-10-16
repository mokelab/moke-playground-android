package com.mokelab.playground.feature.notification.permission

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mokelab.playground.core.ui.BackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationPermissionScreen(
    back: () -> Unit,
) {
    var result by remember { mutableStateOf<Boolean?>(null) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        result = granted
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Permission") },
                navigationIcon = { BackIconButton(onClick = back) },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // POST_NOTIFICATIONS は API 33 (Android 13) で追加された実行時権限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Button(onClick = {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) {
                    Text("Request permission")
                }
                when (result) {
                    true -> Text("Result: granted")
                    false -> Text("Result: denied")
                    null -> Unit
                }
            } else {
                Text("API 33 未満では通知の実行時権限は不要です")
            }
        }
    }
}
