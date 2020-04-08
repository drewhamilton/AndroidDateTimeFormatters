package drewhamilton.androiddatetimeformatters.javatime

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import drewhamilton.androiddatetimeformatters.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime
import java.util.Locale

@RequiresApi(21) // Instrumented tests for Dynamic Features is not supported on API < 21 (AGP 4.0.0-beta04)
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
        assertEquals(FORMATTED_TIME_12, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        timeSetting = "12"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(FORMATTED_TIME_12, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        timeSetting = "24"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(FORMATTED_TIME_24, formattedTime)
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
        assertEquals(FORMATTED_TIME_24, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        timeSetting = "12"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(FORMATTED_TIME_12_ITALY, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        timeSetting = "24"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(FORMATTED_TIME_24, formattedTime)
    }

    private companion object {
        private val TAG = AndroidDateTimeFormatterTest::class.java.simpleName

        private const val SDK_INT_NULLABLE_TIME_SETTING = 28

        private val TIME = LocalTime.of(16, 44)

        private const val FORMATTED_TIME_12 = "4:44 PM"
        private const val FORMATTED_TIME_12_ITALY = "04:44 PM"
        private const val FORMATTED_TIME_24 = "16:44"
    }
}
