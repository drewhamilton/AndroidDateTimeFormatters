package drewhamilton.androiddatetimeformatters.javatime

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import drewhamilton.androiddatetimeformatters.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@RequiresApi(26)
class AndroidDateTimeFormatterTest : TimeSettingTest() {

    @Test fun ofLocalizedTime_nullSystemSettingUsLocale_uses12HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API ${Build.VERSION.SDK_INT}")
            return
        }

        timeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        timeSetting = "12"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        timeSetting = "24"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingItalyLocale_uses24HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API ${Build.VERSION.SDK_INT}")
            return
        }

        timeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        timeSetting = "12"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        timeSetting = "24"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    private val expectedFormattedTime get(): String = androidTimeFormatInUtc.format(LEGACY_TIME)

    private companion object {
        private val TAG = AndroidDateTimeFormatterTest::class.java.simpleName

        private const val SDK_INT_NULLABLE_TIME_SETTING = 28

        private val TIME = LocalTime.of(16, 44)
        @JvmStatic private val LEGACY_TIME: Date = timeFormat24InUtc.parse("16:44")
    }
}

