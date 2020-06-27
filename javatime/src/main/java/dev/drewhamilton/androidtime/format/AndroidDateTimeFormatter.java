package dev.drewhamilton.androidtime.format;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Provides Android-specific {@link DateTimeFormatter}s, such as a localized time formatter that respects the user's
 * 12-/24-hour clock preference.
 */
public final class AndroidDateTimeFormatter {

    private static final String TAG = AndroidDateTimeFormatter.class.getSimpleName();

    //region ofLocalizedTime
    /**
     * Returns a {@link DateTimeFormatter} that can format the time according to the context's locale and the user's
     * 12-/24-hour clock preference. Convenience for {@link #ofLocalizedTime(Context, FormatStyle)} which uses {@link
     * FormatStyle#SHORT}.
     *
     * @param context the context with which the 12-/24-hour preference and the primary locale are determined
     * @return the time formatter
     */
    @NonNull
    public static DateTimeFormatter ofLocalizedTime(@NonNull Context context) {
        return ofLocalizedTime(context, FormatStyle.SHORT);
    }

    /**
     * Returns a {@link DateTimeFormatter} that can format the time according to the context's locale. If {@code
     * timeStyle} is {@link FormatStyle#SHORT}, the formatter also respects the user's 12-/24-hour clock preference.
     * <p>
     * The {@link FormatStyle#FULL} and {@link FormatStyle#LONG} styles typically require a time-zone. When formatting
     * using these styles, a {@link java.time.ZoneId} must be available, either by using {@link java.time.ZonedDateTime}
     * or {@link DateTimeFormatter#withZone}.
     *
     * @param context the context with which the 12-/24-hour preference and the primary locale are determined.
     * @param timeStyle the formatter style to obtain
     * @return the time formatter
     */
    @NonNull
    public static DateTimeFormatter ofLocalizedTime(@NonNull Context context, @NonNull FormatStyle timeStyle) {
        Locale contextPrimaryLocale = extractPrimaryLocale(context);

        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            String pattern = getSystemTimeSettingAwareShortTimePattern(context);
            if (pattern == null) {
                Log.w(TAG, "Couldn't determine time pattern based on system 12-/24-hour setting");
            } else {
                return new DateTimeFormatterBuilder()
                        .appendPattern(pattern)
                        .toFormatter(contextPrimaryLocale)
                        // Match java.time's ofLocalizedTime, which also hard-codes IsoChronology:
                        .withChronology(IsoChronology.INSTANCE);
            }
        }

        return DateTimeFormatter.ofLocalizedTime(timeStyle)
                .withLocale(contextPrimaryLocale);
    }
    //endregion

    //region ofLocalizedDate
    /**
     * Returns a locale specific date format for the ISO chronology.
     * <p>
     * This returns a formatter that will format or parse a date. The exact format pattern used varies by locale.
     * <p>
     * The locale is determined from the formatter. The formatter returned directly by this method will use the provided
     * Context's primary locale.
     * <p>
     * Note that the localized pattern is looked up lazily. This {@code DateTimeFormatter} holds the style required and
     * the locale, looking up the pattern required on demand.
     * <p>
     * The returned formatter has a chronology of ISO set to ensure dates in other calendar systems are correctly
     * converted. It has no override zone and uses the {@link java.time.format.ResolverStyle#SMART SMART} resolver
     * style.
     *
     * @param context the context with which the primary locale is determined.
     * @param dateStyle the formatter style to obtain
     * @return the date formatter
     */
    @NonNull
    public static DateTimeFormatter ofLocalizedDate(@NonNull Context context, @NonNull FormatStyle dateStyle) {
        return DateTimeFormatter.ofLocalizedDate(dateStyle)
                .withLocale(extractPrimaryLocale(context));
    }
    //endregion

    //region ofLocalizedDateTime
    /**
     * Returns a locale specific date-time formatter for the ISO chronology.
     * <p>
     * This returns a formatter that will format or parse a date-time. The exact format pattern used varies by locale.
     * <p>
     * The locale is determined from the formatter. The formatter returned directly by this method will use the provided
     * Context's primary locale.
     * <p>
     * Note that the localized pattern is looked up lazily. This {@code DateTimeFormatter} holds the style required and
     * the locale, looking up the pattern required on demand.
     * <p>
     * The returned formatter has a chronology of ISO set to ensure dates in other calendar systems are correctly
     * converted. It has no override zone and uses the {@link java.time.format.ResolverStyle#SMART SMART} resolver
     * style.
     *
     * @param context the context with which the primary locale is determined
     * @param dateTimeStyle the formatter style to obtain
     * @return the date-time formatter
     */
    @NonNull
    public static DateTimeFormatter ofLocalizedDateTime(@NonNull Context context, @NonNull FormatStyle dateTimeStyle) {
        // If format is SHORT, try system 12-/24-hour setting-specific time format:
        if (dateTimeStyle == FormatStyle.SHORT) {
            DateTimeFormatter systemSpecificFormatter =
                    attemptSystemSettingDateTimeFormatter(context, dateTimeStyle, dateTimeStyle);
            if (systemSpecificFormatter != null)
                return systemSpecificFormatter;
        }

        // Either the format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle)
                .withLocale(extractPrimaryLocale(context));
    }

    /**
     * Returns a locale specific date and time format for the ISO chronology.
     * <p>
     * This returns a formatter that will format or parse a date-time. The exact format pattern used varies by locale.
     * <p>
     * The locale is determined from the formatter. The formatter returned directly by this method will use the provided
     * context's primary locale.
     * <p>
     * Note that the localized pattern is looked up lazily. This {@code DateTimeFormatter} holds the style required and
     * the locale, looking up the pattern required on demand.
     * <p>
     * The returned formatter has a chronology of ISO set to ensure dates in other calendar systems are correctly
     * converted. It has no override zone and uses the {@link java.time.format.ResolverStyle#SMART SMART} resolver
     * style.
     *
     * @param context the context with which the primary locale is determined
     * @param dateStyle the date formatter style to obtain
     * @param timeStyle the time formatter style to obtain
     * @return the date, time or date-time formatter
     */
    @NonNull
    public static DateTimeFormatter ofLocalizedDateTime(@NonNull Context context,
            @NonNull FormatStyle dateStyle, @NonNull FormatStyle timeStyle) {
        // If time format is SHORT, try system 12-/24-hour setting-specific time format:
        if (timeStyle == FormatStyle.SHORT) {
            DateTimeFormatter systemSpecificFormatter =
                    attemptSystemSettingDateTimeFormatter(context, dateStyle, timeStyle);
            if (systemSpecificFormatter != null)
                return systemSpecificFormatter;
        }

        // Either the time format is not SHORT or we otherwise can't insert the system-specific pattern:
        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
                .withLocale(extractPrimaryLocale(context));
    }

    @Nullable
    private static DateTimeFormatter attemptSystemSettingDateTimeFormatter(Context context,
            FormatStyle dateStyle, FormatStyle timeStyle) {
        String timePattern = getSystemTimeSettingAwareShortTimePattern(context);
        if (timePattern != null) {
            Locale contextPrimaryLocale = extractPrimaryLocale(context);

            String defaultDateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle,
                    IsoChronology.INSTANCE, contextPrimaryLocale);
            String defaultTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(null, timeStyle,
                    IsoChronology.INSTANCE, contextPrimaryLocale);

            if (!timePattern.equals(defaultTimePattern) && defaultDateTimePattern.contains(defaultTimePattern)) {
                // We can replace the default time pattern with the system-specific one:
                String dateTimePattern = defaultDateTimePattern.replace(defaultTimePattern, timePattern);
                return new DateTimeFormatterBuilder()
                        .appendPattern(dateTimePattern)
                        .toFormatter(contextPrimaryLocale)
                        // Match java.time's ofLocalizedDateTime, which also hard-codes IsoChronology:
                        .withChronology(IsoChronology.INSTANCE);
            }
        }
        return null;
    }
    //endregion

    @Nullable
    private static String getSystemTimeSettingAwareShortTimePattern(Context context) {
        DateFormat legacyFormat = android.text.format.DateFormat.getTimeFormat(context);

        if (legacyFormat instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) legacyFormat).toPattern();
        } else {
            return null;
        }
    }

    //region ofSkeleton
    /**
     * Returns the best possible localized formatter of the given skeleton for the given context's primary locale. A
     * skeleton is similar to, and uses the same format characters as, a Unicode
     * <a href="http://www.unicode.org/reports/tr35/#Date_Format_Patterns">UTS #35</a> pattern.
     *
     * <p>One difference is that order is irrelevant. For example, "MMMMd" will become "MMMM d" in the {@code en_US}
     * locale, but "d. MMMM" in the {@code de_CH} locale.
     *
     * <p>Note also in that second example that the necessary punctuation for German was added. For the same input in
     * {@code es_ES}, we'd have even more extra text: "d 'de' MMMM".
     *
     * <p>This method will automatically correct for grammatical necessity. Given the same "MMMMd" input, the formatter
     * will use "d LLLL" in the {@code fa_IR} locale, where stand-alone months are necessary. Lengths are preserved
     * where meaningful, so "Md" would give a different result to "MMMd", say, except in a locale such as {@code ja_JP}
     * where there is only one length of month.
     *
     * <p>This method will only use patterns that are in CLDR, and is useful whenever you know what elements you want
     * in your format string but don't want to make your code specific to any one locale.
     *
     * @param context the context with which the primary locale is determined
     * @param skeleton a skeleton as described above
     * @return a formatter with the localized pattern based on the skeleton
     */
    @RequiresApi(18)
    @NonNull
    public static DateTimeFormatter ofSkeleton(@NonNull Context context, @NonNull String skeleton) {
        return ofSkeleton(extractPrimaryLocale(context), skeleton);
    }

    /**
     * Returns the best possible localized formatter of the given skeleton for the given locale. A skeleton is similar
     * to, and uses the same format characters as, a Unicode
     * <a href="http://www.unicode.org/reports/tr35/#Date_Format_Patterns">UTS #35</a> pattern.
     *
     * <p>One difference is that order is irrelevant. For example, "MMMMd" will become "MMMM d" in the {@code en_US}
     * locale, but "d. MMMM" in the {@code de_CH} locale.
     *
     * <p>Note also in that second example that the necessary punctuation for German was added. For the same input in
     * {@code es_ES}, we'd have even more extra text: "d 'de' MMMM".
     *
     * <p>This method will automatically correct for grammatical necessity. Given the same "MMMMd" input, the formatter
     * will use "d LLLL" in the {@code fa_IR} locale, where stand-alone months are necessary. <strong>Warning: core
     * library desugaring does not currently support formatting with 'L'.</strong>
     *
     * <p>Lengths are preserved where meaningful, so "Md" would give a different result to "MMMd", say, except in a
     * locale such as {@code ja_JP} where there is only one length of month.
     *
     * <p>This method will only use patterns that are in CLDR, and is useful whenever you know what elements you want
     * in your format string but don't want to make your code specific to any one locale.
     *
     * @param locale the locale into which the skeleton should be localized
     * @param skeleton a skeleton as described above
     * @return a formatter with the localized pattern based on the skeleton
     */
    @RequiresApi(18)
    @NonNull
    public static DateTimeFormatter ofSkeleton(@NonNull Locale locale, @NonNull String skeleton) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, skeleton);
        return new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .toFormatter(locale);
    }
    //endregion

    @NonNull
    private static Locale extractPrimaryLocale(@NonNull Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList localeList = configuration.getLocales();
            if (!localeList.isEmpty()) {
                locale = localeList.get(0);
            }
        }

        if (locale == null) {
            locale = configuration.locale;
        }

        if (locale == null) {
            locale = Locale.getDefault();
        }

        return locale;
    }

    private AndroidDateTimeFormatter() {
        throw new UnsupportedOperationException();
    }
}
