package drewhamilton.androiddatetimeformatters.threetenbp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.text.format.DateFormat.*;

/**
 * Provides Android-specific {@link DateTimeFormatter}s, such as a localized time formatter that respects the user's
 * 12-/24-hour clock preference.
 */
public final class AndroidDateTimeFormatter {

    /**
     * Returns a {@link DateTimeFormatter} that can format the time according to the context's locale and the user's
     * 12-/24-hour clock preference.
     * @param context the application context
     * @return a {@link DateTimeFormatter} that properly formats the time.
     */
    public static DateTimeFormatter ofLocalizedTime(Context context) {
        return convertLegacyFormat(context, getTimeFormat(context));
    }

    /**
     * Returns a {@link DateTimeFormatter} that can format the date according to the context's locale.
     * @param context the application context
     * @return a {@link DateTimeFormatter} that properly formats the date.
     */
    public static DateTimeFormatter ofLocalizedDateShort(Context context) {
        return convertLegacyFormat(context, getDateFormat(context));
    }

    /**
     * Returns a {@link DateTimeFormatter} that can format the date according to the context's locale.
     * @param context the application context
     * @return a {@link DateTimeFormatter} that properly formats the date.
     */
    public static DateTimeFormatter ofLocalizedDateMedium(Context context) {
        return convertLegacyFormat(context, getMediumDateFormat(context));
    }

    /**
     * Returns a {@link DateTimeFormatter} that can format the date according to the context's locale.
     * @param context the application context
     * @return a {@link DateTimeFormatter} that properly formats the date.
     */
    public static DateTimeFormatter ofLocalizedDateLong(Context context) {
        return convertLegacyFormat(context, getLongDateFormat(context));
    }

    private static DateTimeFormatter convertLegacyFormat(Context context, DateFormat legacyFormat) throws IllegalArgumentException {
        if (legacyFormat instanceof SimpleDateFormat) {
            String pattern = ((SimpleDateFormat) legacyFormat).toPattern();
            return new DateTimeFormatterBuilder()
                    .appendPattern(pattern)
                    .toFormatter(extractLocale(context));
        } else {
            throw new IllegalArgumentException("Unable to convert DateFormat to DateTimeFormatter");
        }
    }

    private static Locale extractLocale(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
