package com.mokelab.playground.feature.notification.channel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mokelab.playground.core.ui.BackIconButton

private const val DEMO_CHANNEL_ID = "channel_demo"
private const val GROUP_ID = "channel_group"
private const val GROUP_CHANNEL_A_ID = "channel_group_a"
private const val GROUP_CHANNEL_B_ID = "channel_group_b"
private const val NOTIFICATION_ID = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationChannelScreen(
    back: () -> Unit,
) {
    val context = LocalContext.current
    var snapshot by remember { mutableStateOf(loadSnapshot(context)) }
    val refresh = { snapshot = loadSnapshot(context) }

    // 設定画面でユーザーが変えた内容を、戻ってきたときに読み直す
    LifecycleResumeEffect(Unit) {
        refresh()
        onPauseOrDispose { }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Channel") },
                navigationIcon = { BackIconButton(onClick = back) },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // NotificationChannel は API 26 (Android 8.0) で追加された。それ未満では Compat の操作は何もしない
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Text("通知チャネルは API 26 以上で使えます")
                return@Column
            }

            StatusSection(snapshot)
            HorizontalDivider()
            ImportanceSection(
                onCreate = { importance ->
                    createDemoChannel(context, importance)
                    refresh()
                },
                onNotify = { showNotification(context) },
            )
            HorizontalDivider()
            SettingsSection(
                onOpenApp = { openAppNotificationSettings(context) },
                onOpenChannel = { openChannelSettings(context, DEMO_CHANNEL_ID) },
            )
            HorizontalDivider()
            GroupSection(
                onCreate = {
                    createGroup(context)
                    refresh()
                },
                onDelete = {
                    NotificationManagerCompat.from(context).deleteNotificationChannelGroup(GROUP_ID)
                    refresh()
                },
            )
            HorizontalDivider()
            DeleteSection(
                onDelete = {
                    NotificationManagerCompat.from(context).deleteNotificationChannel(DEMO_CHANNEL_ID)
                    refresh()
                },
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun Description(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall)
}

@Composable
private fun StatusSection(snapshot: ChannelSnapshot) {
    SectionTitle("状態")
    Text("アプリの通知: ${if (snapshot.notificationsEnabled) "有効" else "無効"}")
    val demo = snapshot.demoChannel
    if (demo == null) {
        Text("$DEMO_CHANNEL_ID: なし")
    } else {
        Text("$DEMO_CHANNEL_ID: name=${demo.name}, importance=${importanceName(demo.importance)}")
    }
    if (snapshot.groups.isEmpty()) {
        Text("グループ: なし")
    }
    snapshot.groups.forEach { group ->
        Text("グループ ${group.id} (${group.name})")
        group.channels.forEach { channel ->
            Text("  - ${channel.id}: ${channel.name}")
        }
    }
}

@Composable
private fun ImportanceSection(
    onCreate: (Int) -> Unit,
    onNotify: () -> Unit,
) {
    SectionTitle("importance の変更")
    Description(
        "同じ ID で作り直すと name はいつでも更新される。importance は「下げる方向」かつ" +
            "「ユーザーが設定を触っていない」ときだけ反映される。LOW → HIGH は効かず、HIGH → LOW は効く。" +
            "削除しても以前の importance が復元されるので、まっさらな状態から試すにはアプリのデータを消去する。"
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onCreate(NotificationManagerCompat.IMPORTANCE_LOW) }) {
            Text("LOW で作成")
        }
        Button(onClick = { onCreate(NotificationManagerCompat.IMPORTANCE_HIGH) }) {
            Text("HIGH で作成")
        }
        OutlinedButton(onClick = onNotify) {
            Text("このチャネルで通知")
        }
    }
}

@Composable
private fun SettingsSection(
    onOpenApp: () -> Unit,
    onOpenChannel: () -> Unit,
) {
    SectionTitle("設定画面を開く")
    Description("設定画面で importance を変えて戻ると状態が更新される。その後「HIGH で作成」しても上書きされない。")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = onOpenApp) {
            Text("アプリの通知設定")
        }
        Button(onClick = onOpenChannel) {
            Text("$DEMO_CHANNEL_ID の設定")
        }
    }
}

@Composable
private fun GroupSection(
    onCreate: () -> Unit,
    onDelete: () -> Unit,
) {
    SectionTitle("チャネルグループ")
    Description("グループに属するチャネルは、アプリの通知設定でグループ名の見出しの下にまとまる。グループを消すと中のチャネルも消える。")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = onCreate) {
            Text("グループを作成")
        }
        OutlinedButton(onClick = onDelete) {
            Text("グループを削除")
        }
    }
}

@Composable
private fun DeleteSection(
    onDelete: () -> Unit,
) {
    SectionTitle("削除と再作成")
    Description(
        "同じ ID で作り直すと、Builder に渡した importance は無視され、削除前の設定がそのまま戻る（設定画面には削除したカテゴリの数が表示される）。" +
            "設定で importance を変える → 削除 → LOW で作成 → 設定画面での値に戻っていることを確かめる。"
    )
    OutlinedButton(onClick = onDelete) {
        Text("$DEMO_CHANNEL_ID を削除")
    }
}

private data class ChannelSnapshot(
    val notificationsEnabled: Boolean,
    val demoChannel: NotificationChannelCompat?,
    val groups: List<NotificationChannelGroupCompat>,
)

private fun loadSnapshot(context: Context): ChannelSnapshot {
    val manager = NotificationManagerCompat.from(context)
    return ChannelSnapshot(
        notificationsEnabled = manager.areNotificationsEnabled(),
        demoChannel = manager.getNotificationChannelCompat(DEMO_CHANNEL_ID),
        groups = manager.notificationChannelGroupsCompat,
    )
}

private fun createDemoChannel(context: Context, importance: Int) {
    val label = importanceName(importance)
    val channel = NotificationChannelCompat.Builder(DEMO_CHANNEL_ID, importance)
        .setName("Demo ($label)")
        .setDescription("importance=$label で作成したチャネル")
        .build()
    NotificationManagerCompat.from(context).createNotificationChannel(channel)
}

private fun createGroup(context: Context) {
    val manager = NotificationManagerCompat.from(context)
    // チャネルが参照するグループは、チャネルより先に作っておく
    manager.createNotificationChannelGroup(
        NotificationChannelGroupCompat.Builder(GROUP_ID)
            .setName("Demo Group")
            .build()
    )
    manager.createNotificationChannelsCompat(
        listOf(
            NotificationChannelCompat.Builder(GROUP_CHANNEL_A_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setName("Group A")
                .setGroup(GROUP_ID)
                .build(),
            NotificationChannelCompat.Builder(GROUP_CHANNEL_B_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setName("Group B")
                .setGroup(GROUP_ID)
                .build(),
        )
    )
}

// POST_NOTIFICATIONS は notification:permission で取得済みという前提
@SuppressLint("MissingPermission")
private fun showNotification(context: Context) {
    // チャネルが存在しない状態で notify すると通知は表示されない
    val notification = NotificationCompat.Builder(context, DEMO_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Channel demo")
        .setContentText("$DEMO_CHANNEL_ID から通知しました")
        .build()
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun openAppNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    context.startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun openChannelSettings(context: Context, channelId: String) {
    val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        .putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
    context.startActivity(intent)
}

private fun importanceName(importance: Int): String = when (importance) {
    NotificationManagerCompat.IMPORTANCE_NONE -> "NONE"
    NotificationManagerCompat.IMPORTANCE_MIN -> "MIN"
    NotificationManagerCompat.IMPORTANCE_LOW -> "LOW"
    NotificationManagerCompat.IMPORTANCE_DEFAULT -> "DEFAULT"
    NotificationManagerCompat.IMPORTANCE_HIGH -> "HIGH"
    else -> "UNSPECIFIED($importance)"
}
