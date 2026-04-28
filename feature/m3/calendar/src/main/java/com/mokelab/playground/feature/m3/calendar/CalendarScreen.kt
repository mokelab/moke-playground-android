package com.mokelab.playground.feature.m3.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    back: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<Long?>(null) }
    val now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val pickerState = rememberDatePickerState(
        selectableDates = object: SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= now
            }
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("M3 Calendar")
                },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Button(onClick = {
                showDialog = true
            }) {
                Text("Show Dialog")
            }

            if (selected != null) {
                Text("Selected: ${SimpleDateFormat("yyyy/MM/dd", Locale.US).format(selected) }")
            }

            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            selected = pickerState.selectedDateMillis
                        }) {
                            Text(stringResource(android.R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                        }) {
                            Text(stringResource(android.R.string.cancel))
                        }
                    },
                ) {
                    DatePicker(
                        state = pickerState,
                    )
                }
            }
        }
    }
}