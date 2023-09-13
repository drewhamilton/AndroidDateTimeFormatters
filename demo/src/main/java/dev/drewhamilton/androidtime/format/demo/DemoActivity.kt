package dev.drewhamilton.androidtime.format.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.drewhamilton.androidtime.format.demo.ui.theme.DemoTheme
import java.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow

class DemoActivity : ComponentActivity() {

    private val latestInstant = MutableStateFlow(Instant.now())

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                val instant by latestInstant.collectAsState()
                Demo(
                    instant = instant,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        latestInstant.value = Instant.now()
    }
}
