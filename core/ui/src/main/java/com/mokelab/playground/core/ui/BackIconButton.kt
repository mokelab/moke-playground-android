package com.mokelab.playground.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/**
 * TopAppBar の navigationIcon に置く「戻る」ボタン。
 *
 * material-icons への依存を避けるため、アイコンは core:ui 内の vector drawable を使う。
 */
@Composable
fun BackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_back_24px),
            contentDescription = "Back",
        )
    }
}
