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
    //region ofLocalizedTime
    /**
     * Returns a [DateTimeFormatter] that can format the time for the ISO chronology according to
     * the [context]'s locale and the user's 12-/24-hour clock preference. Uses [FormatStyle.SHORT].
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
     * [FormatStyle.SHORT], [FormatStyle.MEDIUM], or [FormatStyle.LONG], the formatter also respects
     * the user's 12-/24-hour clock preference.
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
        return ofLocalizedTime(context, context.extractPrimaryLocale(), timeStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the time for the ISO chronology according to
     * the given [locale] and the given [timeStyle]. If [timeStyle] is [FormatStyle.SHORT],
     * [FormatStyle.MEDIUM], or [FormatStyle.LONG], the formatter also respects the user's
     * 12-/24-hour clock preference, determined via [context].
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
            FormatStyle.SHORT, FormatStyle.MEDIUM, FormatStyle.LONG -> {
                getSystemTimeSettingAwareTimePattern(
                    context = context,
                    locale = locale,
                    style = timeStyle,
                )
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
    //endregion

    //region ofLocalizedDate
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
    //endregion

    //region ofLocalizedDateTime
    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the [context]'s primary locale and the given [dateTimeStyle]. If [dateTimeStyle] is
     * [FormatStyle.SHORT], [FormatStyle.MEDIUM], or [FormatStyle.LONG], the formatter also respects
     * the user's 12-/24-hour clock preference, determined via [context].
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
        return ofLocalizedDateTime(context, context.extractPrimaryLocale(), dateTimeStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the given [locale] and the given [dateTimeStyle]. If [dateTimeStyle] is
     * [FormatStyle.SHORT], [FormatStyle.MEDIUM], or [FormatStyle.LONG], the formatter also respects
     * the user's 12-/24-hour clock preference, determined via [context].
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
        return ofLocalizedDateTime(
            context = context,
            locale = locale,
            dateStyle = dateTimeStyle,
            timeStyle = dateTimeStyle,
        )
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the [context]'s primary locale and the given [dateStyle] and [timeStyle]. If [timeStyle]
     * is [FormatStyle.SHORT], [FormatStyle.MEDIUM], or [FormatStyle.LONG], the formatter also
     * respects the user's 12-/24-hour clock preference, determined via [context].
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
        return ofLocalizedDateTime(context, context.extractPrimaryLocale(), dateStyle, timeStyle)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according
     * to the given [locale] and the given [dateStyle] and [timeStyle]. If [timeStyle] is
     * [FormatStyle.SHORT], [FormatStyle.MEDIUM], or [FormatStyle.LONG], the formatter also respects
     * the user's 12-/24-hour clock preference, determined via [context].
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
        val systemTimeSettingAwarePattern = when (timeStyle) {
            FormatStyle.SHORT, FormatStyle.MEDIUM, FormatStyle.LONG -> {
                getSystemTimeSettingAwareDateTimePattern(
                    context = context,
                    locale = locale,
                    dateStyle = dateStyle,
                    timeStyle = timeStyle,
                )
            }

            else -> null
        }

        return if (systemTimeSettingAwarePattern == null) {
            DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
                .withLocale(locale)
        } else {
            DateTimeFormatterBuilder()
                .appendPattern(systemTimeSettingAwarePattern)
                .toFormatter(locale)
                // Match java.time's ofLocalizedDateTime, which also hard-codes IsoChronology:
                .withChronology(IsoChronology.INSTANCE)
        }
    }

    /**
     * Currently only supports [FormatStyle.SHORT], [FormatStyle.MEDIUM], and [FormatStyle.LONG]
     * formats for [timeStyle].
     */
    @JvmStatic private fun getSystemTimeSettingAwareDateTimePattern(
        context: Context,
        locale: Locale,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): String {
        val preferredTimePattern = getSystemTimeSettingAwareTimePattern(
            context = context,
            locale = locale,
            style = timeStyle,
        )

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

        val canReplaceDefaultTimePattern = preferredTimePattern != defaultTimePattern &&
            defaultDateTimePattern.contains(defaultTimePattern)
        return if (canReplaceDefaultTimePattern) {
            defaultDateTimePattern.replace(
                oldValue = defaultTimePattern,
                newValue = preferredTimePattern,
            )
        } else {
            return defaultDateTimePattern
        }
    }
    //endregion

    /**
     * Currently only supports [FormatStyle.SHORT], [FormatStyle.MEDIUM], and [FormatStyle.LONG]
     * formats.
     */
    @JvmStatic private fun getSystemTimeSettingAwareTimePattern(
        context: Context,
        locale: Locale,
        style: FormatStyle,
        systemPattern: String = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            null,
            style,
            IsoChronology.INSTANCE,
            locale,
        ),
    ): String {
        val patternGenerator = DateTimePatternGenerator.getInstance(locale)
        val patternFor12Setting = patternGenerator.getBestPatternFor12Setting(locale, style)
        val patternFor24Setting = patternGenerator.getBestPatternFor24Setting(locale, style)

        val timeSetting = context.timeSetting()
            ?: if (locale.is24HourLocale()) "24" else "12"
        return when (timeSetting) {
            "12" if systemPattern.contains(patternFor12Setting) -> {
                systemPattern
            }

            "24" if systemPattern.contains(patternFor24Setting) -> {
                systemPattern
            }

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
                // Sometimes, for longer formats, the "best" pattern doesn't match the default
                // pattern. In these cases the default pattern often includes a match for a shorter
                // time format, so we recursively try substituting a shorter time format:
                val shorterStyle = when (style) {
                    FormatStyle.FULL -> FormatStyle.LONG
                    FormatStyle.LONG -> FormatStyle.MEDIUM
                    FormatStyle.MEDIUM -> FormatStyle.SHORT
                    FormatStyle.SHORT -> null
                }
                if (shorterStyle == null) {
                    systemPattern
                } else {
                    getSystemTimeSettingAwareTimePattern(
                        context = context,
                        locale = locale,
                        style = shorterStyle,
                        systemPattern = systemPattern,
                    )
                }
            }
        }
    }

    @JvmStatic private fun Context.timeSetting(): String? {
        return if (useTestSystemTimeSetting) {
            testSystemTimeSetting
        } else {
            // Prod code path:
            Settings.System.getString(contentResolver, Settings.System.TIME_12_24)
        }
    }

    @JvmStatic private val unsupportedFormatMessage =
        "getSystemTimeSettingAwareTimePattern only supports SHORT, MEDIUM, and LONG formats"

    @JvmStatic private fun DateTimePatternGenerator.getBestPatternFor12Setting(
        locale: Locale,
        style: FormatStyle,
    ): String {
        val skeletonFor12Setting = when (style) {
            FormatStyle.LONG -> "hmsz"
            FormatStyle.MEDIUM -> "hms"
            FormatStyle.SHORT -> "hm"
            else -> throw IllegalArgumentException(unsupportedFormatMessage)
        }
        return locale.getCompatibleEnglishPattern(
            pattern = getBestPattern(skeletonFor12Setting),
        )
    }

    @JvmStatic private fun DateTimePatternGenerator.getBestPatternFor24Setting(
        locale: Locale,
        style: FormatStyle,
    ): String {
        val skeletonFor12Setting = when (style) {
            FormatStyle.LONG -> "Hmsz"
            FormatStyle.MEDIUM -> "Hms"
            FormatStyle.SHORT -> "Hm"
            else -> throw IllegalArgumentException(unsupportedFormatMessage)
        }
        return locale.getCompatibleEnglishPattern(
            pattern = getBestPattern(skeletonFor12Setting),
        )
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

    //region Test
    private var useTestSystemTimeSetting: Boolean = false
    private var testSystemTimeSetting: String? = null
    //endregion
}
