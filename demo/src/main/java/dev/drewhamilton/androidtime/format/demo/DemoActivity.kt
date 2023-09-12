package dev.drewhamilton.androidtime.format.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dev.drewhamilton.androidtime.format.demo.ui.theme.DemoTheme
import java.time.Instant

class DemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                Demo(
                    instant = Instant.now(),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
