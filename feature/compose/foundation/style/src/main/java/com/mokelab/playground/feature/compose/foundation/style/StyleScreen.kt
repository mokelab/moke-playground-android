package com.mokelab.playground.feature.compose.foundation.style

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
@Composable
internal fun StyleScreen(
    back: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) {
        it.isEnabled = true
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Style API") },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {},
                modifier = Modifier.styleable {
                    background(color = Color.Blue)
                    hovered {
                        background(color = Color.Red)
                    }
                },
            ) {
                Text("OK")
            }
            Text(
                text = "Hello",
                modifier = Modifier
                    .height(48.dp)
                    .clickable(interactionSource = interactionSource, indication = null) {

                    }
                    .styleable(styleState = styleState) {
                        background(color = Color.Red)
                        hovered {
                            background(color = Color.Green)
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun PreviewStyleScreen() {
    StyleScreen(back = {})
}