package drewhamilton.androiddatetimeformatters.test;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A base test class that facilitates using and changing the {@link android.provider.Settings.System#TIME_12_24} setting
 * by caching the current setting before each test and resetting it after.
 * <p>
 * Currently it appears that even though changing this setting requires the
 * {@link android.Manifest.permission#WRITE_SETTINGS} permission, tests work just fine without explicitly requesting it
 * as long as the permission is declared in the test manifest.
 * <p>
 * Know issues:
 *  - Tests fail on APIs 23-28 because {@code null} is an unsupported value by the setter, but is returned by the
 *    getter on new devices.
 *  - 12-hour format tests in e.g. Italy locale fail on API 16. For some reason the Android formatter outputs "4:44 PM"
 *    while the ThreeTenBP formatter outputs "4:44 p.".
 */
public abstract class TimeSettingTest {

    private static final String TAG = TimeSettingTest.class.getSimpleName();

    private String originalHourSetting;

    @Before
    public final void cacheOriginalHourSetting() {
        originalHourSetting = getHourSetting();
        Log.d(TAG, "cached original setting: " + originalHourSetting);
        // Fail test immediately if the device won't allow resetting to the original hour setting:
        resetHourSetting();
    }

    @After
    public final void resetHourSetting() {
        Log.d(TAG, "Resetting original setting: " + originalHourSetting);
        setTimeSetting(originalHourSetting);
    }

    protected final Context getTestContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }

    protected final DateFormat getAndroidTimeFormatInUtc() {
        DateFormat format = android.text.format.DateFormat.getTimeFormat(getTestContext());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    private String getHourSetting() {
        return Settings.System.getString(getTestContext().getContentResolver(), Settings.System.TIME_12_24);
    }

    protected final void setTimeSetting(String hourSetting) {
        Settings.System.putString(getTestContext().getContentResolver(), Settings.System.TIME_12_24, hourSetting);
    }

    protected final void setTestLocale(Locale locale) {
        getTestContext().getResources().getConfiguration().locale = locale;
    }

    protected static DateFormat get24HourTimeFormatInUtc() {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }
}
