package dev.drewhamilton.androidtime.format.demo

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.drewhamilton.androidtime.format.demo.ui.theme.DemoTheme
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.MutableStateFlow

class DemoActivity : ComponentActivity() {

    private val latestZonedDateTime = MutableStateFlow(ZonedDateTime.now())

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(navigationBarStyle = ScrimlessNavigationBarStyle())
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                val zonedDateTime by latestZonedDateTime.collectAsState()
                Demo(
                    zonedDateTime = zonedDateTime,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        latestZonedDateTime.value = ZonedDateTime.now()
    }

    /**
     * Using [SystemBarStyle.auto] automatically adds a scrim in API 29+. This avoids that default
     * by using [SystemBarStyle.light] or [SystemBarStyle.dark] to avoid that.
     */
    @Suppress("FunctionName") // Factory
    private fun ScrimlessNavigationBarStyle(): SystemBarStyle {
        return when {
            Build.VERSION.SDK_INT < 29 -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
            isNightModeActive -> SystemBarStyle.dark(Color.TRANSPARENT)
            else -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        }
    }

    private val Context.isNightModeActive: Boolean
        get() {
            return with(resources.configuration) {
                if (Build.VERSION.SDK_INT >= 30) {
                    isNightModeActive
                } else {
                    (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                }
            }
        }
}
