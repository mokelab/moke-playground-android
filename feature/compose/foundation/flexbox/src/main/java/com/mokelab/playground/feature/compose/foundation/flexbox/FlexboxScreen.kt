package com.mokelab.playground.feature.compose.foundation.flexbox

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFlexBoxApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun FlexBoxScreen(
    back: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flexbox") },
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
        FlexBox(
            config = {
                direction(FlexDirection.Row)
                alignItems(FlexAlignItems.Center)
                justifyContent(FlexJustifyContent.Center)
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .height(64.dp)
            ) {
                Text("Button 1")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .height(32.dp)
            ) {
                Text("Button 2")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFlexBoxScreen() {
    FlexBoxScreen(back = {})
}