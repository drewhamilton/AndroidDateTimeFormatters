package dev.drewhamilton.androidtime.format

import android.os.Build
import androidx.annotation.RequiresApi
import dev.drewhamilton.androidtime.format.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeFalse
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

@RequiresApi(21) // Instrumented tests for Dynamic Features is not supported on API < 21 (AGP 4.0.0-beta04)
class AndroidDateTimeFormatterTest : TimeSettingTest() {

    private val expectedFormattedTime: String
        get() = androidTimeFormatInUtc.format(LEGACY_TIME)

    //region ofLocalizedTime
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
    //endregion

    //region ofLocalizedDate
    @Test fun ofLocalizedDate_usLocaleShortFormat_usesShortUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.SHORT)
        assertEquals("4/24/10", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_usLocaleMediumFormat_usesMediumUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.MEDIUM)
        assertEquals("Apr 24, 2010", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_usLocaleLongFormat_usesLongUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.LONG)
        assertEquals("April 24, 2010", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_usLocaleFullFormat_usesFullUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.FULL)
        assertEquals("Saturday, April 24, 2010", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_italyLocaleShortFormat_usesShortItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.SHORT)
        assertEquals("24/04/10", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_italyLocaleMediumFormat_usesMediumItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.MEDIUM)
        assertEquals("24 apr 2010", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_italyLocaleLongFormat_usesLongItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.LONG)
        assertEquals("24 aprile 2010", formatter.format(DATE))
    }

    @Test fun ofLocalizedDate_italyLocaleFullFormat_usesFullItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.FULL)
        assertEquals("sabato 24 aprile 2010", formatter.format(DATE))
    }
    //endregion

    private companion object {
        private val DATE = LocalDate.of(2010, Month.APRIL, 24)
        private val TIME = LocalTime.of(16, 44)
        private val LEGACY_TIME: Date = TIME_FORMAT_24_IN_UTC.parse("16:44")!!
    }
}
