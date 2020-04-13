package dev.drewhamilton.androidtime.format;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Provides Android-specific {@link DateTimeFormatter}s, such as a localized time formatter that respects the user's
 * 12-/24-hour clock preference.
 */
public final class AndroidDateTimeFormatter {

    /**
     * Returns a {@link DateTimeFormatter} that can format the time according to the context's locale and the user's
     * 12-/24-hour clock preference. Due to the implementation of {@link android.text.format.DateFormat#getTimeFormat},
     * this will always return a {@link FormatStyle#SHORT} time format.
     *
     * @param context the context with which the 12-/24-hour preference and the primary locale are determined.
     * @return a {@link DateTimeFormatter} that properly formats the time.
     */
    @NonNull
    public static DateTimeFormatter ofLocalizedTime(@NonNull Context context) {
        DateFormat legacyFormat = android.text.format.DateFormat.getTimeFormat(context);

        if (legacyFormat instanceof SimpleDateFormat) {
            String pattern = ((SimpleDateFormat) legacyFormat).toPattern();
            return new DateTimeFormatterBuilder()
                    .appendPattern(pattern)
                    .toFormatter(extractPrimaryLocale(context));
        } else {
            // DateFormat.getTimeFormat is hard-coded to be a SimpleDateFormat instance, so this should never happen:
            String errorMessage = String.format(Locale.US,
                    "Expected Android time format to be %s, but it was %s",
                    SimpleDateFormat.class.getName(), legacyFormat.getClass().getName());
            throw new IllegalStateException(errorMessage);
        }
    }

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
        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
                .withLocale(extractPrimaryLocale(context));
    }

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
