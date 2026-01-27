package dev.drewhamilton.androidtime.format.demo

import android.content.res.Configuration
import android.icu.text.NumberFormat
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter
import dev.drewhamilton.androidtime.format.demo.ui.theme.DemoTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class) // Top app bar
@Composable
fun Demo(
    zonedDateTime: ZonedDateTime,
    modifier: Modifier = Modifier,
    initialSelectedTabIndex: Int = 0,
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
        val typedLocaleState = rememberSaveable(saver = TextFieldState.Saver) {
            TextFieldState()
        }
        val locale = parseLocaleString(typedLocaleState.text)
            ?: LocalContext.current.extractPrimaryLocale()

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            val pagerState = rememberPagerState(
                initialPage = initialSelectedTabIndex,
                pageCount = { 2 },
            )
            var requestedPage by remember { mutableIntStateOf(-1) }
            if (requestedPage != -1) {
                LaunchedEffect(requestedPage) {
                    pagerState.animateScrollToPage(page = requestedPage)
                    requestedPage = -1
                }
            }

            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                TextTab(
                    text = "Standard formats",
                    selected = pagerState.currentPage == 0,
                    onClick = { requestedPage = 0 },
                )

                TextTab(
                    text = "Skeleton formats",
                    selected = pagerState.currentPage == 1,
                    onClick = { requestedPage = 1 },
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    when (page) {
                        0 -> StandardFormatDemo(
                            locale = locale,
                            zonedDateTime = zonedDateTime,
                        )

                        1 -> SkeletonFormatDemo(
                            locale = locale,
                            zonedDateTime = zonedDateTime,
                        )
                    }
                }
            }

            LocaleInputField(
                state = typedLocaleState,
                currentLocale = locale,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable private fun TextTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        unselectedContentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun StandardFormatDemo(
    locale: Locale,
    zonedDateTime: ZonedDateTime,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier,
    ) {
        var selectedDateFormat: FormatStyle? by rememberSaveable { mutableStateOf(FormatStyle.LONG) }
        var selectedTimeFormat: FormatStyle? by rememberSaveable { mutableStateOf(FormatStyle.SHORT) }

        FormatComparison(
            locale = locale,
            zonedDateTime = zonedDateTime,
            dateFormatStyle = selectedDateFormat,
            timeFormatStyle = selectedTimeFormat,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

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
    }
}

@Composable
private fun FormatComparison(
    locale: Locale,
    zonedDateTime: ZonedDateTime,
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

            Column {
                Text(
                    text = dateTimeType?.toString() ?: "Epoch millisecond",
                    style = MaterialTheme.typography.titleMedium,
                )
                val systemTimeSetting = Settings.System.getString(
                    LocalContext.current.contentResolver,
                    Settings.System.TIME_12_24,
                )
                Text(
                    text = "System time setting: $systemTimeSetting",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            val dateTimeTextStyle = MaterialTheme.typography.dateTimeTextStyle(
                dateFormatStyle = dateFormatStyle,
                timeFormatStyle = timeFormatStyle,
            )
            if (dateTimeType == null) {
                val numberFormat = NumberFormat.getInstance(locale)
                Text(
                    text = numberFormat.format(zonedDateTime.toInstant().toEpochMilli()),
                    style = dateTimeTextStyle,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                return@Column
            }

            val context = LocalContext.current

            val androidFormatter = remember(
                dateFormatStyle,
                timeFormatStyle,
                dateTimeType,
                locale,
                context,
            ) {
                when (dateTimeType) {
                    DateTimeType.Time -> AndroidDateTimeFormatter.ofLocalizedTime(
                        context = context,
                        locale = locale,
                        timeStyle = timeFormatStyle!!,
                    )

                    DateTimeType.Date -> AndroidDateTimeFormatter.ofLocalizedDate(
                        locale = locale,
                        dateStyle = dateFormatStyle!!,
                    )

                    DateTimeType.DateTime -> AndroidDateTimeFormatter.ofLocalizedDateTime(
                        context = context,
                        locale = locale,
                        dateStyle = dateFormatStyle!!,
                        timeStyle = timeFormatStyle!!,
                    )
                }
            }
            LabeledText(
                label = "AndroidDateTimeFormatter",
                value = androidFormatter.format(zonedDateTime),
                labelStyle = MaterialTheme.typography.labelLarge,
                valueStyle = dateTimeTextStyle,
                modifier = Modifier.weight(1f),
            )

            val standardFormatter = remember(
                dateFormatStyle,
                timeFormatStyle,
                dateTimeType,
                locale,
            ) {
                when (dateTimeType) {
                    DateTimeType.Time -> DateTimeFormatter.ofLocalizedTime(timeFormatStyle)
                    DateTimeType.Date -> DateTimeFormatter.ofLocalizedDate(dateFormatStyle)
                    DateTimeType.DateTime ->
                        DateTimeFormatter.ofLocalizedDateTime(dateFormatStyle, timeFormatStyle)
                }.withLocale(locale)
            }
            LabeledText(
                label = "DateTimeFormatter",
                value = standardFormatter.format(zonedDateTime),
                labelStyle = MaterialTheme.typography.labelLarge,
                valueStyle = dateTimeTextStyle,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private enum class DateTimeType {
    Time,
    Date,
    DateTime {
        override fun toString() = "Date-time"
    },
}

/**
 * Auto-size is constraining values to a single line, so we manually choose a text style instead.
 */
private fun Typography.dateTimeTextStyle(
    dateFormatStyle: FormatStyle?,
    timeFormatStyle: FormatStyle?,
): TextStyle = when (timeFormatStyle) {
    FormatStyle.FULL -> bodyLarge
    FormatStyle.LONG if dateFormatStyle != null -> headlineSmall
    null if dateFormatStyle == null -> headlineLarge
    else -> displayMedium
}

@Composable
private fun LabeledText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = label,
            style = labelStyle,
            fontFamily = FontFamily.Monospace,
        )
        Text(
            text = value,
            autoSize = if (valueStyle == MaterialTheme.typography.displayMedium) {
                TextAutoSize.StepBased(
                    minFontSize = 16.sp,
                    maxFontSize = valueStyle.fontSize,
                    stepSize = 1.sp,
                )
            } else {
                null
            },
            style = valueStyle,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormatStyleSelector(
    selectedFormatStyle: FormatStyle?,
    onFormatStyleSelected: (FormatStyle?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    var showingSelectionPopup by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = showingSelectionPopup,
        onExpandedChange = {},
        modifier = modifier,
    ) {
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

        ExposedDropdownMenu(
            expanded = showingSelectionPopup,
            onDismissRequest = { showingSelectionPopup = false },
            shape = textFieldShape,
            tonalElevation = 3.dp,
            shadowElevation = 0.dp,
        ) {
            val nullableFormatStyles = buildList {
                addAll(FormatStyle.entries)
                add(null)
            }
            nullableFormatStyles.forEach { formatStyle ->
                DropdownMenuItem(
                    text = { Text(formatStyle?.toString() ?: "NONE") },
                    onClick = {
                        onFormatStyleSelected(formatStyle)
                        showingSelectionPopup = false
                    },
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SkeletonFormatDemo(
    locale: Locale,
    zonedDateTime: ZonedDateTime,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier,
    ) {
        val skeletonInputState = rememberSaveable(saver = TextFieldState.Saver)  {
            TextFieldState(initialText = "MMMMd")
        }

        Surface(
            shape = RoundedCornerShape(32.dp),
            tonalElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(16.dp),
            ) {
                var formattingFailure = false
                val formatter = remember(skeletonInputState.text, locale) {
                    try {
                        AndroidDateTimeFormatter.ofSkeleton(
                            skeleton = skeletonInputState.text.toString(),
                            locale = locale,
                        )
                    } catch (_: Exception) {
                        null.also {
                            formattingFailure = true
                        }
                    }
                }
                val formattedDateTime = try {
                    formatter?.format(zonedDateTime) ?: "Invalid format skeleton"
                } catch (_ : Exception) {
                    "Formatting failed".also {
                        formattingFailure = true
                    }
                }

                val dateTimeStyle = MaterialTheme.typography.displayLarge
                Text(
                    text = formattedDateTime,
                    color = LocalContentColor.current.copy(
                        alpha = if (formattingFailure) 0.7f else 1f,
                    ),
                    autoSize = if (formattingFailure) {
                        null
                    } else {
                        TextAutoSize.StepBased(
                            minFontSize = 16.sp,
                            maxFontSize = dateTimeStyle.fontSize,
                            stepSize = 1.sp,
                        )
                    },
                    style = if (formattingFailure) {
                        MaterialTheme.typography.headlineSmall
                    } else {
                        dateTimeStyle
                    },
                )
            }
        }

        OutlinedTextField(
            state = skeletonInputState,
            label = { Text("Date/time format skeleton") },
            placeholder = { Text("Hms") },
            lineLimits = TextFieldLineLimits.SingleLine,
            shape = textFieldShape,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun LocaleInputField(
    state: TextFieldState,
    currentLocale: Locale,
    modifier: Modifier = Modifier,
) {
    var dismissedDropdownText by rememberSaveable { mutableStateOf(state.text) }

    val allLocales = Locale.getAvailableLocales()
    val groupedLocales = allLocales
        .groupBy { locale ->
            val localeString = locale.toString()
            when {
                localeString.startsWith(state.text) -> 0
                localeString.contains(state.text, ignoreCase = true) -> 1
                locale.displayName.contains(state.text, ignoreCase = true) -> 2
                else -> -1
            }
        }
    val filteredLocales = buildList {
        groupedLocales.keys
            .filter { it >= 0 }
            .sorted()
            .forEach { key ->
                groupedLocales[key]?.let { addAll(it) }
            }
    }
    val expanded = dismissedDropdownText != state.text &&
        filteredLocales.isNotEmpty() && state.text.length >= 2
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {},
        modifier = modifier,
    ) {
        OutlinedTextField(
            state = state,
            label = {
                Text(currentLocale.displayName)
            },
            placeholder = {
                Text(currentLocale.toString())
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            shape = textFieldShape,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { dismissedDropdownText = state.text },
            shape = textFieldShape,
            tonalElevation = 3.dp,
            shadowElevation = 0.dp,
        ) {
            filteredLocales.forEach { locale ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = locale.toString(),
                            )
                            Text(
                                text = locale.displayName,
                                color = LocalContentColor.current.copy(alpha = 0.7f),
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    },
                    onClick = {
                        val newText = locale.toString()
                        state.setTextAndPlaceCursorAtEnd(newText)
                        dismissedDropdownText = newText
                    },
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private val textFieldShape = RoundedCornerShape(16.dp)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StandardFormatDemoPreview() {
    DemoTheme {
        Demo(
            zonedDateTime = ZonedDateTime.now(),
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SkeletonFormatDemoPreview() {
    DemoTheme {
        Demo(
            zonedDateTime = ZonedDateTime.now(),
            initialSelectedTabIndex = 1,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
