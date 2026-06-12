package com.mokelab.playground.feature.compose.webview.form

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class WebViewActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("WebView") }
                    )
                },
            ) { innerPadding ->
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            loadData(html, "text/html", "UTF-8")
                        }
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .imePadding(),
                )
            }
        }
    }
}

private const val inputCount = 100

private val html = buildString {
    append(
        """
        <html>
            <body>
                <h1>WebView Form</h1>
                <form>
        """.trimIndent()
    )

    repeat(inputCount) { index ->
        val fieldNumber = index + 1
        append("<label for=\"field$fieldNumber\">Field $fieldNumber:</label><br>")
        append("<input type=\"text\" id=\"field$fieldNumber\" name=\"field$fieldNumber\"><br><br>")
    }

    append(
        """
                <input type="submit" value="Submit">
                </form>
            </body>
        </html>
        """.trimIndent()
    )
}
