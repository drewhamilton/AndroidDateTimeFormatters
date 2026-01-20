package dev.drewhamilton.androidtime.format

import android.content.Context
import android.icu.text.DateTimePatternGenerator
import android.icu.util.ULocale
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
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
    ): DateTimeFormatter {
        return ofLocalizedTime(context, FormatStyle.SHORT)
    }

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
    @JvmStatic fun ofLocalizedTime(
        context: Context,
        locale: Locale,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        val systemTimeSettingAwarePattern = when (timeStyle) {
            FormatStyle.SHORT -> {
                getSystemTimeSettingAwareShortTimePattern(context, locale)
            }

            FormatStyle.MEDIUM -> {
                val timeSetting = context.timeSetting()
                if (timeSetting == null) {
                    // FIXME: This returns null by default on API 25, and differs from the
                    //  behavior of the short time pattern (e.g. shows 5:08 PM on medium, but
                    //  17:08 on short).
                    null
                } else {
                    val patternGenerator = DateTimePatternGenerator.getInstance(locale)
                    val patternFor12Setting = locale.getCompatibleEnglishPattern(
                        pattern = patternGenerator.getBestPattern("hms"),
                    )
                    val patternFor24Setting = locale.getCompatibleEnglishPattern(
                        pattern = patternGenerator.getBestPattern("Hms"),
                    )
                    val systemPattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                        null,
                        timeStyle,
                        IsoChronology.INSTANCE,
                        locale,
                    )
                    when (timeSetting) {
                        "12" if systemPattern.contains(patternFor24Setting) -> {
                            systemPattern.replace(
                                oldValue = patternFor24Setting,
                                newValue = patternFor12Setting,
                            )
                        }

                        "24" if systemPattern.contains(patternFor12Setting) -> {
                            systemPattern.replace(
                                oldValue = patternFor12Setting,
                                newValue = patternFor24Setting,
                            )
                        }

                        else -> {
                            systemPattern
                        }
                    }
                }
            }

            else -> null
        }
        return if (systemTimeSettingAwarePattern == null) {
            DateTimeFormatter.ofLocalizedTime(timeStyle)
                .withLocale(locale)
        } else {
            DateTimeFormatterBuilder()
                .appendPattern(systemTimeSettingAwarePattern)
                .toFormatter(locale)
                // Match java.time's ofLocalizedTime, which also hard-codes IsoChronology:
                .withChronology(IsoChronology.INSTANCE)
        }
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
        return ofLocalizedDate(context.extractPrimaryLocale(), dateStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date for the ISO chronology according to
     * the given [locale] and [dateStyle].
     *
     * The formatter's pattern may be eagerly determined, so appending
     * [DateTimeFormatter.withLocale] to the returned formatter is not guaranteed to fully localize
     * the result.
     */
    @JvmStatic fun ofLocalizedDate(
        locale: Locale,
        dateStyle: FormatStyle,
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(dateStyle)
            .withLocale(locale)
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
        @Suppress("NewApi") // Safely using Context locale
        return ofLocalizedDateTime(context, context.extractPrimaryLocale(), dateTimeStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the given [locale] and the given [dateTimeStyle]. If [dateTimeStyle] is
     * [FormatStyle.SHORT], the formatter also respects the user's 12-/24-hour clock preference,
     * determined via [context].
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
        locale: Locale,
        dateTimeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (dateTimeStyle == FormatStyle.SHORT) {
            val systemSpecificFormatter = attemptSystemSettingDateTimeFormatter(
                context = context,
                locale = locale,
                dateStyle = dateTimeStyle,
                timeStyle = dateTimeStyle,
            )
            if (systemSpecificFormatter != null) return systemSpecificFormatter
        }

        // Either the format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle)
            .withLocale(locale)
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
        @Suppress("NewApi") // Safely using Context locale
        return ofLocalizedDateTime(context, context.extractPrimaryLocale(), dateStyle, timeStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the given [locale] and the given [dateStyle] and [timeStyle]. If [timeStyle] is
     * [FormatStyle.SHORT], the formatter also respects the user's 12-/24-hour clock preference,
     * determined via [context].
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
        locale: Locale,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If time format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            val systemSpecificFormatter = attemptSystemSettingDateTimeFormatter(
                context = context,
                locale = locale,
                dateStyle = dateStyle,
                timeStyle = timeStyle,
            )
            if (systemSpecificFormatter != null) return systemSpecificFormatter
        }

        // The time format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
            .withLocale(locale)
    }

    @JvmStatic private fun attemptSystemSettingDateTimeFormatter(
        context: Context,
        locale: Locale,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): DateTimeFormatter? {
        val timePattern = getSystemTimeSettingAwareShortTimePattern(context, locale)

        val defaultDateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            dateStyle,
            timeStyle,
            IsoChronology.INSTANCE,
            locale,
        )
        val defaultTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            null,
            timeStyle,
            IsoChronology.INSTANCE,
            locale,
        )

        val canReplaceDefaultTimePattern = timePattern != defaultTimePattern &&
            defaultDateTimePattern.contains(defaultTimePattern)
        if (canReplaceDefaultTimePattern) {
            val dateTimePattern: String = defaultDateTimePattern.replace(
                oldValue = defaultTimePattern,
                newValue = timePattern,
            )
            return DateTimeFormatterBuilder()
                .appendPattern(dateTimePattern)
                .toFormatter(locale)
                // Match java.time's ofLocalizedDateTime, which also hard-codes IsoChronology:
                .withChronology(IsoChronology.INSTANCE)
        }

        return null
    }

    @JvmStatic private fun getSystemTimeSettingAwareShortTimePattern(
        context: Context,
        locale: Locale,
    ): String {
        val timeSetting = context.timeSetting()
            ?: if (locale.is24HourLocale()) "24" else "12"
        val patternGenerator = DateTimePatternGenerator.getInstance(locale)
        val patternSkeleton = when (timeSetting) {
            "12" -> "hm"
            "24" -> "Hm"
            else -> throw IllegalArgumentException("Unknown time setting: $timeSetting")
        }
        val bestPattern = patternGenerator.getBestPattern(patternSkeleton)
        return locale.getCompatibleEnglishPattern(bestPattern)
    }

    @JvmStatic private fun Context.timeSetting(): String? {
        // TODO: Support testing without a static field?
        return when (val testSystemTimeSetting = testSystemTimeSetting) {
            // Prod code path:
            is TestSystemTimeSetting.Unset -> {
                Settings.System.getString(contentResolver, Settings.System.TIME_12_24)
            }

            // Test code path:
            is TestSystemTimeSetting.Set -> {
                testSystemTimeSetting.value
            }
        }
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

    /**
     * Adapted from [android.text.format.DateFormat] internals; a fix for the private bug
     * http://b/266731719. Replaces '\u202f' (NNBSP) with a normal space if the locale's language is
     * English and if the locale's region is empty or "US".
     *
     * No-op on API 34+. At this API level, NNBSP is used commonly throughout date/time format
     * strings, so the private bug is presumed fixed.
     */
    @JvmStatic private fun Locale.getCompatibleEnglishPattern(pattern: String): String {
        if (Build.VERSION.SDK_INT >= 34) {
            return pattern
        }

        if (language != "en") {
            return pattern
        }

        val uLocale = ULocale.forLocale(this)
        val region = uLocale.country
        return if (region == null || region.isEmpty() || region == "US") {
            pattern.replace('\u202f', ' ')
        } else {
            pattern
        }
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
        val localeList = configuration.locales
        if (!localeList.isEmpty) {
            locale = localeList[0]
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
