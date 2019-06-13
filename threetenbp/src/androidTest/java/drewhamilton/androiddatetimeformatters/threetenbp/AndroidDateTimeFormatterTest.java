package drewhamilton.androiddatetimeformatters.threetenbp;

import android.os.Build;
import android.util.Log;
import drewhamilton.androiddatetimeformatters.test.TimeSettingTest;
import org.junit.Test;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class AndroidDateTimeFormatterTest extends TimeSettingTest {

    private static final String TAG = AndroidDateTimeFormatterTest.class.getSimpleName();

    private static final int SDK_INT_NULLABLE_TIME_SETTING = 28;

    private static final LocalTime TIME = LocalTime.of(16, 44);
    private static final Date LEGACY_TIME;

    static {
        try {
            LEGACY_TIME = getTimeFormat24InUtc().parse("16:44");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ofLocalizedTime_nullSystemSettingUsLocale_uses12HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API " + Build.VERSION.SDK_INT);
            return;
        }

        setTimeSetting(null);
        setTestLocale(Locale.US);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(expectedFormattedTime(), formattedTime);
    }

    @Test
    public void ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        setTimeSetting("12");
        setTestLocale(Locale.US);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(expectedFormattedTime(), formattedTime);
    }

    @Test
    public void ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        setTimeSetting("24");
        setTestLocale(Locale.US);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(expectedFormattedTime(), formattedTime);
    }

    @Test
    public void ofLocalizedTime_nullSystemSettingItalyLocale_uses24HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API " + Build.VERSION.SDK_INT);
            return;
        }

        setTimeSetting(null);
        setTestLocale(Locale.ITALY);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(expectedFormattedTime(), formattedTime);
    }

    @Test
    public void ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        setTimeSetting("12");
        setTestLocale(Locale.ITALY);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(expectedFormattedTime(), formattedTime);
    }

    @Test
    public void ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        setTimeSetting("24");
        setTestLocale(Locale.ITALY);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(expectedFormattedTime(), formattedTime);
    }

    private String expectedFormattedTime() {
        return getAndroidTimeFormatInUtc().format(LEGACY_TIME);
    }
}
