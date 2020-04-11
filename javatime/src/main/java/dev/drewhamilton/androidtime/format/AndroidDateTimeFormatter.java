package dev.drewhamilton.androidtime.format;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

/**
 * Provides Android-specific {@link DateTimeFormatter}s, such as a localized time formatter that respects the user's
 * 12-/24-hour clock preference.
 */
public final class AndroidDateTimeFormatter {

    /**
     * Returns a {@link DateTimeFormatter} that can format the time according to the context's locale and the user's
     * 12-/24-hour clock preference.
     *
     * @param context the application context
     * @return a {@link DateTimeFormatter} that properly formats the time.
     */
    public static DateTimeFormatter ofLocalizedTime(Context context) {
        DateFormat legacyFormat = android.text.format.DateFormat.getTimeFormat(context);

        if (legacyFormat instanceof SimpleDateFormat) {
            String pattern = ((SimpleDateFormat) legacyFormat).toPattern();
            return new DateTimeFormatterBuilder()
                    .appendPattern(pattern)
                    .toFormatter(extractLocale(context));
        } else {
            // DateFormat.getTimeFormat is hard-coded to be a SimpleDateFormat instance, so this should never happen:
            String errorMessage = String.format(
                    Locale.US,
                    "Expected Android time format to be %s, but it was %s",
                    SimpleDateFormat.class.getName(), legacyFormat.getClass().getName());
            throw new IllegalStateException(errorMessage);
        }
    }

    private static Locale extractLocale(Context context) {
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
