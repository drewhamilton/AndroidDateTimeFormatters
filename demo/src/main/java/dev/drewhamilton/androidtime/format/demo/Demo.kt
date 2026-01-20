package dev.drewhamilton.androidtime.format.demo

import android.content.res.Configuration
import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
        var typedLocaleString by remember { mutableStateOf("") }
        val locale = parseLocaleString(typedLocaleString)
            ?: LocalContext.current.extractPrimaryLocale()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = contentPadding + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            val fillMaxWidthModifier = Modifier.fillMaxWidth()
            item {
                LocaleInputField(
                    value = typedLocaleString,
                    onValueChange = { typedLocaleString = it },
                    currentLocale = locale,
                    modifier = fillMaxWidthModifier,
                )
            }

            item {
                FormatComparison(
                    locale = locale,
                    instant = instant,
                    dateTimeType = DateTimeType.Time,
                    formatStyle = FormatStyle.SHORT,
                    modifier = fillMaxWidthModifier,
                )
            }

            item {
                FormatComparison(
                    locale = locale,
                    instant = instant,
                    dateTimeType = DateTimeType.Time,
                    formatStyle = FormatStyle.MEDIUM,
                    modifier = fillMaxWidthModifier,
                )
            }

            item {
                FormatComparison(
                    locale = locale,
                    instant = instant,
                    dateTimeType = DateTimeType.DateTime,
                    formatStyle = FormatStyle.LONG,
                    modifier = fillMaxWidthModifier,
                )
            }

            item {
                FormatComparison(
                    locale = locale,
                    instant = instant,
                    dateTimeType = DateTimeType.Date,
                    formatStyle = FormatStyle.FULL,
                    modifier = fillMaxWidthModifier,
                )
            }
        }
    }
}

private fun parseLocaleString(value: String): Locale? {
    val parts = value.split('_')
    return when (parts.size) {
        0 -> null
        1 -> localeOf(language = parts.single())
        2 -> localeOf(language = parts[0], country = parts[1])
        3 -> localeOf(language = parts[0], country = parts[1], variant = parts[2])
        else -> null
    }?.let {
        if (it.toLanguageTag() == "und") {
            null
        } else {
            it
        }
    }
}

private fun localeOf(language: String): Locale {
    return if (Build.VERSION.SDK_INT >= 36) {
        Locale.of(language)
    } else {
        @Suppress("DEPRECATION")
        Locale(language)
    }
}

private fun localeOf(language: String, country: String): Locale {
    return if (Build.VERSION.SDK_INT >= 36) {
        Locale.of(language, country)
    } else {
        @Suppress("DEPRECATION")
        Locale(language, country)
    }
}

private fun localeOf(language: String, country: String, variant: String): Locale {
    return if (Build.VERSION.SDK_INT >= 36) {
        Locale.of(language, country, variant)
    } else {
        @Suppress("DEPRECATION")
        Locale(language, country, variant)
    }
}

@Composable
private fun LocaleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    currentLocale: Locale,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = {
            Text(currentLocale.displayName)
        },
        placeholder = {
            Text(currentLocale.toString())
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun FormatComparison(
    locale: Locale,
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
                    AndroidDateTimeFormatter.ofLocalizedTime(context, locale, formatStyle)

                DateTimeType.Date ->
                    AndroidDateTimeFormatter.ofLocalizedDate(locale, formatStyle)

                DateTimeType.DateTime ->
                    AndroidDateTimeFormatter.ofLocalizedDateTime(context, locale, formatStyle)
            }
            LabeledText(
                label = "AndroidDateTimeFormatter",
                value = androidFormatter.format(zonedDateTime),
            )

            val standardFormatter = when (dateTimeType) {
                DateTimeType.Time -> DateTimeFormatter.ofLocalizedTime(formatStyle)
                DateTimeType.Date -> DateTimeFormatter.ofLocalizedDate(formatStyle)
                DateTimeType.DateTime -> DateTimeFormatter.ofLocalizedDateTime(formatStyle)
            }.withLocale(locale)
            LabeledText(
                label = "DateTimeFormatter",
                value = standardFormatter.format(zonedDateTime),
            )

            if (formatStyle == FormatStyle.SHORT) {
                val usingSpecifiedLocale = locale == context.extractPrimaryLocale()

                val legacyDateFormat = AndroidTextDateFormat.getTimeFormat(context)
                val legacyDate = JavaUtilDate(instant.toEpochMilli())
                LabeledText(
                    label = "android.text.format.DateFormat",
                    value = legacyDateFormat.format(legacyDate),
                    modifier = Modifier.alpha(if (usingSpecifiedLocale) 1f else 0.5f),
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
