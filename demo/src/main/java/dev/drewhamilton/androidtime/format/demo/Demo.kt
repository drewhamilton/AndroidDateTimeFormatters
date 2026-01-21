package dev.drewhamilton.androidtime.format.demo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter
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

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            LocaleInputField(
                value = typedLocaleString,
                onValueChange = { typedLocaleString = it },
                currentLocale = locale,
                modifier = Modifier.fillMaxWidth(),
            )

            var selectedDateFormat: FormatStyle? by remember { mutableStateOf(FormatStyle.LONG) }
            var selectedTimeFormat: FormatStyle? by remember { mutableStateOf(FormatStyle.SHORT) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FormatStyleSelector(
                    selectedFormatStyle = selectedDateFormat,
                    onFormatStyleSelected = { selectedDateFormat = it },
                    label = "Date format style",
                    modifier = Modifier.weight(1f),
                )
                FormatStyleSelector(
                    selectedFormatStyle = selectedTimeFormat,
                    onFormatStyleSelected = { selectedTimeFormat = it },
                    label = "Time format style",
                    modifier = Modifier.weight(1f),
                )
            }

            FormatComparison(
                locale = locale,
                instant = instant,
                dateFormatStyle = selectedDateFormat,
                timeFormatStyle = selectedTimeFormat,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
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
        shape = textFieldShape,
    )
}

@Composable
private fun FormatStyleSelector(
    selectedFormatStyle: FormatStyle?,
    onFormatStyleSelected: (FormatStyle?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    var popupWidthPx by remember { mutableIntStateOf(0) }
    Layout(
        modifier = modifier,
        content = {
            var showingSelectionPopup by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = selectedFormatStyle?.toString() ?: "NONE",
                onValueChange = { /* No-op, value change is handled by dropdown menu */ },
                readOnly = true,
                label = { Text(label) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.arrow_drop_down_24dp),
                        contentDescription = null,
                    )
                },
                singleLine = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                if (interaction is PressInteraction.Release) {
                                    showingSelectionPopup = true
                                }
                            }
                        }
                    },
                shape = textFieldShape,
            )

            DropdownMenu(
                expanded = showingSelectionPopup,
                onDismissRequest = { showingSelectionPopup = false },
                shape = textFieldShape,
            ) {
                val nullableFormatStyles = buildList {
                    addAll(FormatStyle.entries)
                    add(null)
                }
                val popupWidth = with(LocalDensity.current) { popupWidthPx.toDp() }
                nullableFormatStyles.forEach { formatStyle ->
                    Text(
                        text = formatStyle?.toString() ?: "NONE",
                        modifier = Modifier
                            .clickable {
                                onFormatStyleSelected(formatStyle)
                                showingSelectionPopup = false
                            }
                            .width(popupWidth)
                            .padding(16.dp),
                    )
                }
            }
        },
    ) { measurables, constraints ->
        val includesPopup = measurables.size > 1
        val placeables = measurables.mapIndexed { i, measurable ->
            val isPopup = includesPopup && i == measurables.lastIndex
            val childConstraints = if (isPopup) {
                constraints.copy(minWidth = constraints.maxWidth)
            } else {
                constraints
            }
            measurable.measure(childConstraints)
        }
        val placeablesWithoutPopup = if (includesPopup) placeables.dropLast(1) else placeables
        val height = placeablesWithoutPopup.maxOf { it.height }
        layout(width = constraints.maxWidth, height = height) {
            popupWidthPx = constraints.maxWidth
            placeables.forEachIndexed { i, placeable ->
                val isPopup = includesPopup && i == placeables.lastIndex
                val yOffset = if (isPopup) {
                    constraints.minHeight
                } else {
                    0
                }
                placeable.placeRelative(x = 0, y = yOffset)
            }
        }
    }
}

private val textFieldShape = RoundedCornerShape(16.dp)

@Composable
private fun FormatComparison(
    locale: Locale,
    instant: Instant,
    dateFormatStyle: FormatStyle?,
    timeFormatStyle: FormatStyle?,
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
            val dateTimeType = when {
                dateFormatStyle != null && timeFormatStyle != null -> DateTimeType.DateTime
                dateFormatStyle != null -> DateTimeType.Date
                timeFormatStyle != null -> DateTimeType.Time
                else -> null
            }

            Text(
                text = "$dateTimeType",
                style = MaterialTheme.typography.titleMedium,
            )

            val context = LocalContext.current

            val zone = ZoneId.systemDefault()
            val zonedDateTime = remember(instant, zone) { instant.atZone(zone) }

            val androidFormatter = when (dateTimeType) {
                DateTimeType.Time ->
                    AndroidDateTimeFormatter.ofLocalizedTime(
                        context = context,
                        locale = locale,
                        timeStyle = timeFormatStyle!!,
                    )

                DateTimeType.Date ->
                    AndroidDateTimeFormatter.ofLocalizedDate(
                        locale = locale,
                        dateStyle = dateFormatStyle!!,
                    )

                DateTimeType.DateTime ->
                    AndroidDateTimeFormatter.ofLocalizedDateTime(
                        context = context,
                        locale = locale,
                        dateStyle = dateFormatStyle!!,
                        timeStyle = timeFormatStyle!!,
                    )

                null -> null
            }
            androidFormatter?.let {
                LabeledText(
                    label = "AndroidDateTimeFormatter",
                    value = it.format(zonedDateTime),
                    modifier = Modifier.weight(1f),
                )
            }

            val standardFormatter = when (dateTimeType) {
                DateTimeType.Time -> DateTimeFormatter.ofLocalizedTime(timeFormatStyle)
                DateTimeType.Date -> DateTimeFormatter.ofLocalizedDate(dateFormatStyle)
                DateTimeType.DateTime ->
                    DateTimeFormatter.ofLocalizedDateTime(dateFormatStyle, timeFormatStyle)
                null -> null
            }?.withLocale(locale)
            standardFormatter?.let {
                LabeledText(
                    label = "DateTimeFormatter",
                    value = it.format(zonedDateTime),
                    modifier = Modifier.weight(1f),
                )
            }

            if (dateFormatStyle == null && timeFormatStyle == FormatStyle.SHORT) {
                val usingSpecifiedLocale = locale == context.extractPrimaryLocale()

                val legacyDateFormat = AndroidTextDateFormat.getTimeFormat(context)
                val legacyDate = JavaUtilDate(instant.toEpochMilli())
                LabeledText(
                    label = "android.text.format.DateFormat",
                    value = legacyDateFormat.format(legacyDate),
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (usingSpecifiedLocale) 1f else 0.5f),
                )
            } else {
                Spacer(Modifier.weight(1f))
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
