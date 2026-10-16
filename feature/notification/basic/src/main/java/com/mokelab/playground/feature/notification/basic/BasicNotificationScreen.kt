package com.mokelab.playground.feature.notification.basic

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mokelab.playground.core.ui.BackIconButton

private const val CHANNEL_ID = "basic"
private const val NOTIFICATION_ID = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BasicNotificationScreen(
    back: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Basic Notification") },
                navigationIcon = { BackIconButton(onClick = back) },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Button(onClick = { showNotification(context) }) {
                Text("Show notification")
            }
        }
    }
}

// POST_NOTIFICATIONS は notification:permission で取得済みという前提
@SuppressLint("MissingPermission")
private fun showNotification(context: Context) {
    val manager = NotificationManagerCompat.from(context)

    // API 26 未満では何もしない
    val channel = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
        .setName("Basic")
        .build()
    manager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Hello")
        .setContentText("This is a basic notification")
        .build()
    // 同じ ID で notify すると新しく追加されず、既存の通知が更新される
    manager.notify(NOTIFICATION_ID, notification)
}
