package drewhamilton.androiddatetimeformatters.test

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * A base test class that facilitates using and changing the [Settings.System.TIME_12_24] setting by caching the current
 * setting before each test and resetting it after.
 *
 * Currently it appears that even though changing this setting requires the [android.Manifest.permission.WRITE_SETTINGS]
 * permission, tests work just fine without explicitly requesting it as long as the permission is declared in the test
 * manifest.
 *
 * Know issues:
 *  - Tests fail on APIs 23-27 because null is an unsupported value by the setter, but is returned by the
 *    getter on new devices.
 *  - 12-hour format tests in e.g. Italy locale fail on API 16. For some reason the Android formatter outputs "4:44 PM"
 *    while the ThreeTenBP formatter outputs "4:44 p.", despite an identical format patter of "h:mm a".
 */
abstract class TimeSettingTest {

    private var originalTimeSetting: String? = null

    @Before
    fun cacheOriginalTimeSetting() {
        originalTimeSetting = timeSetting
        Log.d(TAG, "cached original setting: $originalTimeSetting")
        // Fail test immediately if the device won't allow resetting to the original hour setting:
        resetTimeSetting()
    }

    @After
    fun resetTimeSetting() {
        Log.d(TAG, "Resetting original setting: $originalTimeSetting")
        timeSetting = originalTimeSetting
    }

    protected val testContext get(): Context = InstrumentationRegistry.getInstrumentation().context

    protected val androidFormatTimeInUtc get(): DateFormat =
        android.text.format.DateFormat.getTimeFormat(testContext)
                .apply { timeZone = TimeZone.getTimeZone("UTC") }

    protected val androidFormatDateShort get(): DateFormat =
        android.text.format.DateFormat.getDateFormat(testContext)

    protected val androidFormatDateMedium get(): DateFormat =
        android.text.format.DateFormat.getMediumDateFormat(testContext)

    protected val androidFormatDateLong get(): DateFormat =
        android.text.format.DateFormat.getLongDateFormat(testContext)

    protected var timeSetting: String?
        get() = Settings.System.getString(testContext.contentResolver, Settings.System.TIME_12_24)
        set(value) { Settings.System.putString(testContext.contentResolver, Settings.System.TIME_12_24, value) }

    protected var testLocale: Locale
        get() = testContext.resources.configuration.locale
        set(value) { testContext.resources.configuration.locale = value }

    protected companion object {
        private val TAG = TimeSettingTest::class.java.simpleName

        @JvmStatic val dateTimeFormatInUtc get(): DateFormat =
            SimpleDateFormat("yyyy-MM-dd_HH:mm", Locale.US)
                    .apply { timeZone = TimeZone.getTimeZone("UTC") }
    }
}
