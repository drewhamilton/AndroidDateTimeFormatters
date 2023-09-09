package dev.drewhamilton.androidtime.format

import android.content.Context
import android.os.Build
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
 * Provides Android-specific [DateTimeFormatter]s, such as a localized time formatter that respects the user's
 * 12-/24-hour clock preference.
 */
object AndroidDateTimeFormatter {

    private val Tag: String = AndroidDateTimeFormatter::class.java.simpleName

    //region ofLocalized*
    /**
     * Returns a [DateTimeFormatter] that can format the time for the ISO chronology according to the [context]'s locale
     * and the user's 12-/24-hour clock preference. Overload which uses [FormatStyle.SHORT].
     */
    // TODO: Can `open` be removed and retain compatibility?
    @JvmStatic open fun ofLocalizedTime(
        context: Context,
    ): DateTimeFormatter = ofLocalizedTime(context, FormatStyle.SHORT)

    /**
     * Creates a [DateTimeFormatter] that can format the time for the ISO chronology according to the [context]'s
     * primary locale and the given [timeStyle]. If [timeStyle] is [FormatStyle.SHORT], the formatter also respects the user's 12-/24-hour clock
     * preference.
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] styles typically require a time zone. When formatting using these
     * styles, a [java.time.ZoneId] must be available, either by using [java.time.ZonedDateTime] or
     * [DateTimeFormatter.withZone].
     */
    @JvmStatic open fun ofLocalizedTime(
        context: Context,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        val contextPrimaryLocale = context.extractPrimaryLocale()

        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            val pattern = getSystemTimeSettingAwareShortTimePattern(context)
            if (pattern == null) {
                Log.w(Tag, "Couldn't determine time pattern based on system 12-/24-hour setting")
            } else {
                return DateTimeFormatterBuilder()
                    .appendPattern(pattern)
                    .toFormatter(contextPrimaryLocale) // Match java.time's ofLocalizedTime, which also hard-codes IsoChronology:
                    .withChronology(IsoChronology.INSTANCE)
            }
        }
        return DateTimeFormatter.ofLocalizedTime(timeStyle)
            .withLocale(contextPrimaryLocale)
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date for the ISO chronology according to the [context]'s
     * primary locale and the given [dateStyle].
     */
    @JvmStatic open fun ofLocalizedDate(
        context: Context,
        dateStyle: FormatStyle,
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(dateStyle)
            .withLocale(context.extractPrimaryLocale())
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according to the [context]'s
     * primary locale and the given [dateTimeStyle]. If [dateTimeStyle] is [FormatStyle.SHORT], the formatter also
     * respects the user's 12-/24-hour clock preference.
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] styles typically require a time zone. When formatting using these
     * styles, a [java.time.ZoneId] must be available, either by using [java.time.ZonedDateTime] or
     * [DateTimeFormatter.withZone].
     */
    @JvmStatic open fun ofLocalizedDateTime(
        context: Context,
        dateTimeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (dateTimeStyle == FormatStyle.SHORT) {
            val systemSpecificFormatter =
                attemptSystemSettingDateTimeFormatter(context, dateTimeStyle, dateTimeStyle)
            if (systemSpecificFormatter != null) return systemSpecificFormatter
        }

        // Either the format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle)
            .withLocale(context.extractPrimaryLocale())
    }

    /**
     * Creates a [DateTimeFormatter] that can format the date-time for the ISO chronology according to the [context]'s
     * primary locale and the given [dateStyle] and [timeStyle]. If [timeStyle] is [FormatStyle.SHORT], the formatter
     * also respects the user's 12-/24-hour clock preference.
     *
     * The [FormatStyle.FULL] and [FormatStyle.LONG] time styles typically require a time zone. When formatting using
     * these styles, a [java.time.ZoneId] must be available, either by using [java.time.ZonedDateTime] or
     * [DateTimeFormatter.withZone].
     */
    @JvmStatic open fun ofLocalizedDateTime(
        context: Context,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): DateTimeFormatter {
        // If time format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            val systemSpecificFormatter = attemptSystemSettingDateTimeFormatter(context, dateStyle, timeStyle)
            if (systemSpecificFormatter != null) return systemSpecificFormatter
        }

        // Either the time format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
            .withLocale(context.extractPrimaryLocale())
    }

    @JvmStatic private fun attemptSystemSettingDateTimeFormatter(
        context: Context,
        dateStyle: FormatStyle,
        timeStyle: FormatStyle,
    ): DateTimeFormatter? {
        val timePattern = getSystemTimeSettingAwareShortTimePattern(context)
        if (timePattern != null) {
            val contextPrimaryLocale = context.extractPrimaryLocale()
            val defaultDateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                dateStyle, timeStyle,
                IsoChronology.INSTANCE, contextPrimaryLocale
            )
            val defaultTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                null, timeStyle,
                IsoChronology.INSTANCE, contextPrimaryLocale
            )
            if (timePattern != defaultTimePattern && defaultDateTimePattern.contains(defaultTimePattern)) {
                // We can replace the default time pattern with the system-specific one:
                val dateTimePattern: String = defaultDateTimePattern.replace(defaultTimePattern, timePattern)
                return DateTimeFormatterBuilder()
                    .appendPattern(dateTimePattern)
                    .toFormatter(contextPrimaryLocale) // Match java.time's ofLocalizedDateTime, which also hard-codes IsoChronology:
                    .withChronology(IsoChronology.INSTANCE)
            }
        }
        return null
    }

    @JvmStatic private fun getSystemTimeSettingAwareShortTimePattern(context: Context): String? {
        val legacyFormat = DateFormat.getTimeFormat(context)
        return if (legacyFormat is SimpleDateFormat) {
            legacyFormat.toPattern()
        } else {
            null
        }
    }
    //endregion

    //region ofSkeleton
    /**
     * Creates the best possible localized [DateTimeFormatter] of the given [skeleton] for the given [context]'s primary
     * locale. A skeleton is similar to, and uses the same format characters as, a Unicode
     * [UTS #35](http://www.unicode.org/reports/tr35/#Date_Format_Patterns) pattern.
     *
     * One difference is that order is irrelevant. For example, "MMMMd" will become "MMMM d" in the `en_US`
     * locale, but "d. MMMM" in the `de_CH` locale. Note also in that second example that the necessary punctuation for
     * German was added. For the same input in `es_ES`, we'd have even more extra text: "d 'de' MMMM".
     *
     * This function will automatically correct for grammatical necessity. Given the same "MMMMd" input, the formatter
     * will use "d LLLL" in the `fa_IR` locale, where stand-alone months are necessary. **Warning: core library
     * desugaring does not currently support formatting with 'L'.**
     *
     * Lengths are preserved where meaningful, so "Md" would give a different result to "MMMd", say, except in a locale
     * such as `ja_JP` where there is only one length of month.
     *
     * This function will only use patterns that are in CLDR, and is useful whenever you know what elements you want
     * in your format string but don't want to make your code specific to any one locale.
     */
    @RequiresApi(18)
    @JvmStatic open fun ofSkeleton(
        skeleton: String,
        context: Context,
    ): DateTimeFormatter {
        return ofSkeleton(skeleton, context.extractPrimaryLocale())
    }

    /**
     * Creates the best possible localized [DateTimeFormatter] of the given [skeleton] for the given [locale]. A
     * skeleton is similar to, and uses the same format characters as, a Unicode
     * [UTS #35](http://www.unicode.org/reports/tr35/#Date_Format_Patterns) pattern.
     *
     * One difference is that order is irrelevant. For example, "MMMMd" will become "MMMM d" in the `en_US`
     * locale, but "d. MMMM" in the `de_CH` locale. Note also in that second example that the necessary punctuation for
     * German was added. For the same input in `es_ES`, we'd have even more extra text: "d 'de' MMMM".
     *
     * This function will automatically correct for grammatical necessity. Given the same "MMMMd" input, the formatter
     * will use "d LLLL" in the `fa_IR` locale, where stand-alone months are necessary. **Warning: core library
     * desugaring does not currently support formatting with 'L'.**
     *
     * Lengths are preserved where meaningful, so "Md" would give a different result to "MMMd", say, except in a locale
     * such as `ja_JP` where there is only one length of month.
     *
     * This function will only use patterns that are in CLDR, and is useful whenever you know what elements you want
     * in your format string but don't want to make your code specific to any one locale.
     */
    @RequiresApi(18)
    @JvmStatic open fun ofSkeleton(
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
            @Suppress("DEPRECATION") // Fallback to newer API approach
            locale = configuration.locale
        }
        if (locale == null) {
            locale = Locale.getDefault()
        }
        return locale!!
    }
}
