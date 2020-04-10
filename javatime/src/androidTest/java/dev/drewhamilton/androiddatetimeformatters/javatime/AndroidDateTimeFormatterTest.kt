package dev.drewhamilton.androiddatetimeformatters.javatime

import android.os.Build
import androidx.annotation.RequiresApi
import dev.drewhamilton.androiddatetimeformatters.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeFalse
import org.junit.Test
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@RequiresApi(21) // Instrumented tests for Dynamic Features is not supported on API < 21 (AGP 4.0.0-beta04)
class AndroidDateTimeFormatterTest : TimeSettingTest() {

    @Test fun ofLocalizedTime_nullSystemSettingUsLocale_uses12HourFormat() {
        assumeFalse(
            "Time setting is not nullable in API ${Build.VERSION.SDK_INT}",
            Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING
        )

        systemTimeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        systemTimeSetting = "12"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        systemTimeSetting = "24"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingItalyLocale_uses24HourFormat() {
        assumeFalse(
            "Time setting is not nullable in API ${Build.VERSION.SDK_INT}",
            Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING
        )

        systemTimeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        systemTimeSetting = "12"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        systemTimeSetting = "24"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    private val expectedFormattedTime get(): String = androidTimeFormatInUtc.format(LEGACY_TIME)

    private companion object {
        private const val SDK_INT_NULLABLE_TIME_SETTING = 28

        private val TIME = LocalTime.of(16, 44)
        private val LEGACY_TIME: Date = timeFormat24InUtc.parse("16:44")!!
    }
}
