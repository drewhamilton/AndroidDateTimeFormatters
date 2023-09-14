package dev.drewhamilton.androidtime.format

import android.content.Context
import android.content.res.Configuration
import android.icu.text.DateTimePatternGenerator
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Provides Android-specific [DateTimeFormatter]s, such as a localized time formatter that respects
 * the user's 12-/24-hour clock preference.
 */
object AndroidDateTimeFormatter {

    private val Tag: String = AndroidDateTimeFormatter::class.java.simpleName

    //region ofLocalized*
    /**
     * Returns a [DateTimeFormatter] that can format the time for the ISO chronology according to
     * the [context]'s locale and the user's 12-/24-hour clock preference. Overload which uses
     * [FormatStyle.SHORT].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @JvmStatic fun ofLocalizedTime(
        context: Context,
    ): DateTimeFormatter = ofLocalizedTime(context, FormatStyle.SHORT)

    /**
     * Creates a [DateTimeFormatter] that can format the time for the ISO chronology according to
     * the [context]'s primary locale and the given [timeStyle]. If [timeStyle] is
     * [FormatStyle.SHORT], the formatter also respects the user's 12-/24-hour clock preference.
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] styles typically require a time zone. When
     * formatting using these styles, a [java.time.ZoneId] must be available, either by using
     * [java.time.ZonedDateTime] or [DateTimeFormatter.withZone].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @JvmStatic fun ofLocalizedTime(
        context: Context,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        @Suppress("NewApi") // Safely using Context locale
        return ofLocalizedTime(context, context.extractPrimaryLocale(), timeStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the time for the ISO chronology according to
     * the given [locale] and the given [timeStyle]. If [timeStyle] is [FormatStyle.SHORT], the
     * formatter also respects the user's 12-/24-hour clock preference, determined via [context].
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] styles typically require a time zone. When
     * formatting using these styles, a [java.time.ZoneId] must be available, either by using
     * [java.time.ZonedDateTime] or [DateTimeFormatter.withZone].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @RequiresApi(17) // Can only use Context locale on APIs < 17
    @JvmStatic fun ofLocalizedTime(
        context: Context,
        locale: Locale,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            val pattern = getSystemTimeSettingAwareShortTimePattern(context, locale)
            if (pattern == null) {
                Log.w(Tag, "Couldn't determine time pattern based on system 12-/24-hour setting")
            } else {
                return DateTimeFormatterBuilder()
                    .appendPattern(pattern)
                    .toFormatter(locale)
                    // Match java.time's ofLocalizedTime, which also hard-codes IsoChronology:
                    .withChronology(IsoChronology.INSTANCE)
            }
        }
        return DateTimeFormatter.ofLocalizedTime(timeStyle)
            .withLocale(locale)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date for the ISO chronology according to
     * the [context]'s primary locale and the given [dateStyle].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @JvmStatic fun ofLocalizedDate(
        context: Context,
        dateStyle: FormatStyle,
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(dateStyle)
            .withLocale(context.extractPrimaryLocale())
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the [context]'s primary locale and the given [dateTimeStyle]. If [dateTimeStyle] is
     * [FormatStyle.SHORT], the formatter also respects the user's 12-/24-hour clock preference.
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] styles typically require a time zone. When
     * formatting using these styles, a [java.time.ZoneId] must be available, either by using
     * [java.time.ZonedDateTime] or [DateTimeFormatter.withZone].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @JvmStatic fun ofLocalizedDateTime(
        context: Context,
        dateTimeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (dateTimeStyle == FormatStyle.SHORT) {
            val systemSpecificFormatter = attemptSystemSettingDateTimeFormatter(
                context = context,
                dateStyle = dateTimeStyle,
                timeStyle = dateTimeStyle,
            )
            if (systemSpecificFormatter != null) return systemSpecificFormatter
        }

        // Either the format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle)
            .withLocale(context.extractPrimaryLocale())
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the [context]'s primary locale and the given [dateStyle] and [timeStyle]. If [timeStyle]
     * is [FormatStyle.SHORT], the formatter also respects the user's 12-/24-hour clock preference.
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] time styles typically require a time zone. When
     * formatting using these styles, a [java.time.ZoneId] must be available, either by using
     * [java.time.ZonedDateTime] or [DateTimeFormatter.withZone].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @JvmStatic fun ofLocalizedDateTime(
        context: Context,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If time format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            val systemSpecificFormatter = attemptSystemSettingDateTimeFormatter(
                context = context,
                dateStyle = dateStyle,
                timeStyle = timeStyle,
            )
            if (systemSpecificFormatter != null) return systemSpecificFormatter
        }

        // The time format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
            .withLocale(context.extractPrimaryLocale())
    }

    @JvmStatic private fun attemptSystemSettingDateTimeFormatter(
        context: Context,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): DateTimeFormatter? {
        val timePattern = getLegacySystemTimeSettingAwareShortTimePattern(context) ?: return null

        val contextPrimaryLocale = context.extractPrimaryLocale()
        val defaultDateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            dateStyle,
            timeStyle,
            IsoChronology.INSTANCE,
            contextPrimaryLocale,
        )
        val defaultTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            null,
            timeStyle,
            IsoChronology.INSTANCE,
            contextPrimaryLocale,
        )

        val canReplaceDefaultTimePattern = timePattern != defaultTimePattern &&
            defaultDateTimePattern.contains(defaultTimePattern)
        if (canReplaceDefaultTimePattern) {
            // We can replace the default time pattern with the system-specific one:
            val dateTimePattern: String = defaultDateTimePattern.replace(
                oldValue = defaultTimePattern,
                newValue = timePattern,
            )
            return DateTimeFormatterBuilder()
                .appendPattern(dateTimePattern)
                .toFormatter(contextPrimaryLocale)
                // Match java.time's ofLocalizedDateTime, which also hard-codes IsoChronology:
                .withChronology(IsoChronology.INSTANCE)
        }

        return null
    }

    /**
     * [locale] is ignored on APIs < 17.
     */
    @JvmStatic private fun getSystemTimeSettingAwareShortTimePattern(
        context: Context,
        locale: Locale,
    ): String? {
        val timeSetting = context.timeSetting() ?: if (locale.is24HourLocale()) "24" else "12"
        return when {
            Build.VERSION.SDK_INT >= 24 -> {
                val patternGenerator = DateTimePatternGenerator.getInstance(locale)
                val patternSkeleton = when (timeSetting) {
                    "12" -> "hm"
                    "24" -> "Hm"
                    else -> throw IllegalArgumentException("Unknown time setting: $timeSetting")
                }
                patternGenerator.getBestPattern(patternSkeleton)
            }

            Build.VERSION.SDK_INT >= 17 -> {
                val localeConfiguration = Configuration(context.resources.configuration).apply {
                    @Suppress("DEPRECATION") // API < 24
                    this.locale = locale
                }
                val localeContext = context.createConfigurationContext(localeConfiguration)
                getLegacySystemTimeSettingAwareShortTimePattern(context = localeContext)
            }

            else -> {
                getLegacySystemTimeSettingAwareShortTimePattern(context = context)
            }
        }
    }

    @JvmStatic private fun Context.timeSetting(): String? {
        return Settings.System.getString(contentResolver, Settings.System.TIME_12_24)
    }

    private fun Locale.is24HourLocale(): Boolean {
        val natural = java.text.DateFormat.getTimeInstance(java.text.DateFormat.LONG, this)
        return if (natural is SimpleDateFormat)
            natural.toPattern().hasDesignator('H')
        else
            false
    }

    private fun CharSequence?.hasDesignator(designator: Char): Boolean {
        if (this == null)
            return false

        var insideQuote = false
        forEach { c ->
            if (c == '\'') {
                insideQuote = !insideQuote
            } else if (!insideQuote) {
                if (c == designator)
                    return true
            }
        }

        return false
    }

    @JvmStatic private fun getLegacySystemTimeSettingAwareShortTimePattern(
        context: Context
    ): String? {
        val legacyFormat = DateFormat.getTimeFormat(context) as? SimpleDateFormat
        return legacyFormat?.toLocalizedPattern()
    }
    //endregion

    //region ofSkeleton
    /**
     * Creates the best possible localized [DateTimeFormatter] of the given [skeleton] for the given
     * [context]'s primary locale. A skeleton is similar to, and uses the same format characters as,
     * a Unicode [UTS #35](http://www.unicode.org/reports/tr35/#Date_Format_Patterns) pattern.
     *
     * One difference is that order is irrelevant. For example, "MMMMd" will become "MMMM d" in the
     * `en_US` locale, but "d. MMMM" in the `de_CH` locale. Note also in that second example that
     * the necessary punctuation for German was added. For the same input in `es_ES`, we'd have even
     * more extra text: "d 'de' MMMM".
     *
     * This function will automatically correct for grammatical necessity. Given the same "MMMMd"
     * input, the formatter will use "d LLLL" in the `fa_IR` locale, where stand-alone months are
     * necessary. **Warning: core library desugaring does not currently support formatting with
     * 'L'.**
     *
     * Lengths are preserved where meaningful, so "Md" would give a different result to "MMMd", say,
     * except in a locale such as `ja_JP` where there is only one length of month.
     *
     * This function will only use patterns that are in CLDR, and is useful whenever you know what
     * elements you want in your format string but don't want to make your code specific to any one
     * locale.
     *
     * The formatter's pattern is eagerly determined, so appending [DateTimeFormatter.withLocale] to
     * the returned formatter is not guaranteed to fully localize the result.
     */
    @RequiresApi(18)
    @JvmStatic fun ofSkeleton(
        skeleton: String,
        context: Context,
    ): DateTimeFormatter {
        return ofSkeleton(skeleton, context.extractPrimaryLocale())
    }

    /**
     * Creates the best possible localized [DateTimeFormatter] of the given [skeleton] for the given
     * [locale]. A skeleton is similar to, and uses the same format characters as, a Unicode
     * [UTS #35](http://www.unicode.org/reports/tr35/#Date_Format_Patterns) pattern.
     *
     * One difference is that order is irrelevant. For example, "MMMMd" will become "MMMM d" in the
     * `en_US` locale, but "d. MMMM" in the `de_CH` locale. Note also in that second example that
     * the necessary punctuation for German was added. For the same input in `es_ES`, we'd have even
     * more extra text: "d 'de' MMMM".
     *
     * This function will automatically correct for grammatical necessity. Given the same "MMMMd"
     * input, the formatter will use "d LLLL" in the `fa_IR` locale, where stand-alone months are
     * necessary. **Warning: core library desugaring does not currently support formatting with
     * 'L'.**
     *
     * Lengths are preserved where meaningful, so "Md" would give a different result to "MMMd", say,
     * except in a locale such as `ja_JP` where there is only one length of month.
     *
     * This function will only use patterns that are in CLDR, and is useful whenever you know what
     * elements you want in your format string but don't want to make your code specific to any one
     * locale.
     *
     * The formatter's pattern is eagerly determined, so appending [DateTimeFormatter.withLocale] to
     * the returned formatter is not guaranteed to fully localize the result.
     */
    @RequiresApi(18)
    @JvmStatic fun ofSkeleton(
        skeleton: String,
        locale: Locale,
    ): DateTimeFormatter {
        val pattern = DateFormat.getBestDateTimePattern(locale, skeleton)
        return DateTimeFormatter.ofPattern(pattern, locale)
    }
    //endregion

    @JvmStatic private fun Context.extractPrimaryLocale(): Locale {
        val configuration = resources.configuration
        var locale: Locale? = null
        if (Build.VERSION.SDK_INT >= 24) {
            val localeList = configuration.locales
            if (!localeList.isEmpty) {
                locale = localeList[0]
            }
        }
        if (locale == null) {
            @Suppress("DEPRECATION") // Fallback for newer API approach
            locale = configuration.locale
        }
        if (locale == null) {
            locale = Locale.getDefault()
        }
        return locale!!
    }
}
