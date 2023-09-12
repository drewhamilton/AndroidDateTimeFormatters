package dev.drewhamilton.androidtime.format.demo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter
import dev.drewhamilton.androidtime.format.demo.theme.DemoTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import android.text.format.DateFormat as AndroidTextDateFormat
import java.util.Date as JavaUtilDate

@Composable
fun Demo(
    instant: Instant,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val zone = ZoneId.systemDefault()
    val time = remember(instant, zone) { instant.atZone(zone).toLocalTime() }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            val formatter = AndroidDateTimeFormatter.ofLocalizedTime(context, FormatStyle.SHORT)
            val formattedTime = remember(formatter, time) {
                formatter.format(time)
            }
            LabeledText(
                label = "Short time AndroidDateTimeFormatter",
                value = formattedTime,
            )
        }

        item {
            val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            val formattedTime = remember(formatter, time) {
                formatter.format(time)
            }
            LabeledText(
                label = "Short time DateTimeFormatter",
                value = formattedTime,
            )
        }

        item {
            val formatter = remember(context) { AndroidTextDateFormat.getTimeFormat(context) }
            val legacyDate = JavaUtilDate(instant.toEpochMilli())
            val formattedLegacyDate = remember(formatter, legacyDate) {
                formatter.format(legacyDate)
            }
            LabeledText(
                label = "java.util.Date short time",
                value = formattedLegacyDate,
            )
        }
    }
}

@Composable
private fun LabeledText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.displaySmall,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GreetingPreview() {
    DemoTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            Demo(
                instant = Instant.now(),
            )
        }
    }
}
