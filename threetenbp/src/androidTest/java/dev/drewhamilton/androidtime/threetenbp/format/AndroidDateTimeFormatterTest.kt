package dev.drewhamilton.androidtime.threetenbp.format

import android.os.Build
import dev.drewhamilton.androidtime.format.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeFalse
import org.junit.Test
import org.threeten.bp.LocalTime
import java.util.Date
import java.util.Locale

class AndroidDateTimeFormatterTest : TimeSettingTest() {

    private val expectedFormattedTime: String
        get() = androidTimeFormatInUtc.format(LEGACY_TIME)

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
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
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
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    private companion object {
        private val TIME = LocalTime.of(16, 44)
        private val LEGACY_TIME: Date = TIME_FORMAT_24_IN_UTC.parse("16:44")!!
    }
}
