package dev.drewhamilton.androidtime.format.demo

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter
import dev.drewhamilton.androidtime.format.demo.ui.plus
import dev.drewhamilton.androidtime.format.demo.ui.theme.DemoTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import android.text.format.DateFormat as AndroidTextDateFormat
import java.util.Date as JavaUtilDate

@OptIn(ExperimentalMaterial3Api::class) // Top app bar
@Composable
fun Demo(
    instant: Instant,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        var typedLocale by remember { mutableStateOf("") }
        val overrideLocale = parseLocaleString(typedLocale)
        val context = if (overrideLocale == null) {
            LocalContext.current
        } else {
            Log.d("Demo", "Override locale: $overrideLocale")
            LocalContext.current.copyWithLocale(overrideLocale)
        }
        CompositionLocalProvider(
            LocalContext provides context,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = contentPadding + PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                val fillMaxWidthModifier = Modifier.fillMaxWidth()
                item {
                    LocaleOverrideField(
                        value = typedLocale,
                        onValueChange = { typedLocale = it },
                        modifier = fillMaxWidthModifier,
                    )
                }

                item {
                    FormatComparison(
                        instant = instant,
                        dateTimeType = DateTimeType.Time,
                        formatStyle = FormatStyle.SHORT,
                        modifier = fillMaxWidthModifier,
                    )
                }

                item {
                    FormatComparison(
                        instant = instant,
                        dateTimeType = DateTimeType.Time,
                        formatStyle = FormatStyle.MEDIUM,
                        modifier = fillMaxWidthModifier,
                    )
                }

                item {
                    FormatComparison(
                        instant = instant,
                        dateTimeType = DateTimeType.DateTime,
                        formatStyle = FormatStyle.LONG,
                        modifier = fillMaxWidthModifier,
                    )
                }

                item {
                    FormatComparison(
                        instant = instant,
                        dateTimeType = DateTimeType.Date,
                        formatStyle = FormatStyle.FULL,
                        modifier = fillMaxWidthModifier,
                    )
                }
            }
        }
    }
}

private fun parseLocaleString(value: String): Locale? {
    val parts = value.split('_')
    return when (parts.size) {
        0 -> null
        1 -> Locale(parts.single())
        2 -> Locale(parts[0], parts[1])
        3 -> Locale(parts[0], parts[1], parts[2])
        else -> null
    }?.let {
        if (it.toLanguageTag() == "und") {
            null
        } else {
            it
        }
    }
}

@Composable
private fun LocaleOverrideField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = LocalContext.current.extractPrimaryLocale()
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = {
            Text(locale.displayName)
        },
        placeholder = {
            Text(locale.toString())
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun FormatComparison(
    instant: Instant,
    dateTimeType: DateTimeType,
    formatStyle: FormatStyle,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "$dateTimeType, ${formatStyle.toString().lowercase()}",
                style = MaterialTheme.typography.titleMedium,
            )

            val context = LocalContext.current

            val zone = ZoneId.systemDefault()
            val zonedDateTime = remember(instant, zone) { instant.atZone(zone) }

            val androidFormatter = when (dateTimeType) {
                DateTimeType.Time ->
                    AndroidDateTimeFormatter.ofLocalizedTime(context, formatStyle)

                DateTimeType.Date ->
                    AndroidDateTimeFormatter.ofLocalizedDate(context, formatStyle)

                DateTimeType.DateTime ->
                    AndroidDateTimeFormatter.ofLocalizedDateTime(context, formatStyle)
            }
            LabeledText(
                label = "AndroidDateTimeFormatter",
                value = androidFormatter.format(zonedDateTime),
            )

            val standardFormatter = when (dateTimeType) {
                DateTimeType.Time -> DateTimeFormatter.ofLocalizedTime(formatStyle)
                DateTimeType.Date -> DateTimeFormatter.ofLocalizedDate(formatStyle)
                DateTimeType.DateTime -> DateTimeFormatter.ofLocalizedDateTime(formatStyle)
            }.withLocale(context.extractPrimaryLocale())
            LabeledText(
                label = "DateTimeFormatter",
                value = standardFormatter.format(zonedDateTime),
            )

            if (formatStyle == FormatStyle.SHORT) {
                val legacyDateFormat = AndroidTextDateFormat.getTimeFormat(context)
                val legacyDate = JavaUtilDate(instant.toEpochMilli())
                LabeledText(
                    label = "android.text.format.DateFormat",
                    value = legacyDateFormat.format(legacyDate),
                )
            }
        }
    }
}

private enum class DateTimeType {
    Time,
    Date,
    DateTime {
        override fun toString() = "Date-time"
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
            style = MaterialTheme.typography.labelMedium,
            fontFamily = FontFamily.Monospace,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun GreetingPreview() {
    DemoTheme {
        Demo(
            instant = Instant.now(),
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
