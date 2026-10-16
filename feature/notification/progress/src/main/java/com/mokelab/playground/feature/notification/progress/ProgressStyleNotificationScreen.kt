package com.mokelab.playground.feature.notification.progress

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.mokelab.playground.core.ui.BackIconButton
import kotlinx.coroutines.delay

private const val CHANNEL_ID = "progress"
private const val NOTIFICATION_ID = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProgressStyleNotificationScreen(
    back: () -> Unit,
) {
    val context = LocalContext.current
    val supported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA

    var shown by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    var styledByProgress by rememberSaveable { mutableStateOf(true) }
    var indeterminate by rememberSaveable { mutableStateOf(false) }

    // 表示中に値が変わったら同じ ID で notify して更新する。
    // Slider をドラッグ中は値が連続で変わるので、少し待ってから notify する(間引き)
    LaunchedEffect(shown, progress, styledByProgress, indeterminate) {
        if (!shown || Build.VERSION.SDK_INT < Build.VERSION_CODES.BAKLAVA) return@LaunchedEffect
        delay(100)
        showNotification(
            context = context,
            progress = progress.toInt(),
            styledByProgress = styledByProgress,
            indeterminate = indeterminate,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ProgressStyle Notification") },
                navigationIcon = { BackIconButton(onClick = back) },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            if (!supported) {
                Text("ProgressStyle requires Android 16 (API 36) or later.")
            }
            Row {
                Button(
                    onClick = { shown = true },
                    enabled = supported,
                ) {
                    Text("Show")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {
                        shown = false
                        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
                    },
                    enabled = supported,
                ) {
                    Text("Cancel")
                }
            }
            Text("Progress: ${progress.toInt()}")
            Slider(
                value = progress,
                onValueChange = { progress = it },
                valueRange = 0f..100f,
                enabled = supported,
            )
            SwitchRow(
                label = "styledByProgress",
                checked = styledByProgress,
                onCheckedChange = { styledByProgress = it },
                enabled = supported,
            )
            SwitchRow(
                label = "indeterminate",
                checked = indeterminate,
                onCheckedChange = { indeterminate = it },
                enabled = supported,
            )
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}

// POST_NOTIFICATIONS は notification:permission で取得済みという前提
@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.BAKLAVA)
private fun showNotification(
    context: Context,
    progress: Int,
    styledByProgress: Boolean,
    indeterminate: Boolean,
) {
    val manager = NotificationManagerCompat.from(context)

    val channel = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
        .setName("Progress")
        .build()
    manager.createNotificationChannel(channel)

    // バー全体の長さ(max)はセグメントの長さの合計になる。ここでは 20 + 10 + 70 = 100
    val style = Notification.ProgressStyle()
        .setProgressSegments(
            listOf(
                Notification.ProgressStyle.Segment(20).setColor(Color.BLUE),   // 店へ向かう
                Notification.ProgressStyle.Segment(10).setColor(Color.YELLOW), // 受け取り
                Notification.ProgressStyle.Segment(70).setColor(Color.GREEN),  // 配達
            )
        )
        .setProgressPoints(
            listOf(
                Notification.ProgressStyle.Point(20).setColor(Color.RED),
                Notification.ProgressStyle.Point(30).setColor(Color.RED),
            )
        )
        .setProgress(progress)
        .setProgressIndeterminate(indeterminate)
        .setStyledByProgress(styledByProgress)
        .setProgressTrackerIcon(Icon.createWithResource(context, R.drawable.ic_tracker))
        .setProgressStartIcon(Icon.createWithResource(context, R.drawable.ic_store))
        .setProgressEndIcon(Icon.createWithResource(context, R.drawable.ic_home))

    val notification = Notification.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Delivering")
        .setContentText("$progress / 100")
        .setOngoing(true)
        // 更新のたびに音やバイブが鳴らないようにする
        .setOnlyAlertOnce(true)
        .setStyle(style)
        .build()
    manager.notify(NOTIFICATION_ID, notification)
}
