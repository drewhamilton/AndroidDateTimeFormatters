package drewhamilton.androiddatetimeformatters.javatime;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import drewhamilton.androiddatetimeformatters.test.TimeSettingTest;
import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RequiresApi(26)
public class AndroidDateTimeFormatterTest extends TimeSettingTest {

    private static final String TAG = AndroidDateTimeFormatterTest.class.getSimpleName();

    private static final int SDK_INT_NULLABLE_TIME_SETTING = 28;

    private static final LocalTime TIME = LocalTime.of(16, 44);

    private static final String FORMATTED_TIME_12 = "4:44 PM";
    private static final String FORMATTED_TIME_24 = "16:44";

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
        assertEquals(FORMATTED_TIME_12, formattedTime);
    }

    @Test
    public void ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        setTimeSetting("12");
        setTestLocale(Locale.US);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(FORMATTED_TIME_12, formattedTime);
    }

    @Test
    public void ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        setTimeSetting("24");
        setTestLocale(Locale.US);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(FORMATTED_TIME_24, formattedTime);
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
        assertEquals(FORMATTED_TIME_24, formattedTime);
    }

    @Test
    public void ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        setTimeSetting("12");
        setTestLocale(Locale.ITALY);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(FORMATTED_TIME_12, formattedTime);
    }

    @Test
    public void ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        setTimeSetting("24");
        setTestLocale(Locale.ITALY);

        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getTestContext());
        String formattedTime = formatter.format(TIME);
        assertEquals(FORMATTED_TIME_24, formattedTime);
    }
}
