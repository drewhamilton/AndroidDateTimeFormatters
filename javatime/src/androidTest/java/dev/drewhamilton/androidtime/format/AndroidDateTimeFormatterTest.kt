package dev.drewhamilton.androidtime.format

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.common.truth.Truth.assertThat
import dev.drewhamilton.androidtime.format.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeFalse
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneOffset
import java.time.ZonedDateTime
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

    //region ofLocalizedDateTime with dateTimeStyle
    @Test fun ofLocalizedDateTime_usLocaleShortDateTimeFormat_usesShortUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("4/24/10")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_usLocaleMediumDateTimeFormat_usesMediumUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("Apr 24, 2010")
        assertThat(result).contains("4:44:00 PM")
    }

    @Test fun ofLocalizedDateTime_usLocaleLongDateTimeFormat_usesLongUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("April 24, 2010")
        assertThat(result).contains("4:44:00 PM Z")
    }

    @Test fun ofLocalizedDateTime_usLocaleFullDateTimeFormat_usesFullUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.FULL)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("Saturday, April 24, 2010")
        assertThat(result).contains("4:44:00 PM Z")
    }

    @Test fun ofLocalizedDateTime_italyLocaleShortDateTimeFormat_usesShortItalyFormat() {
        testLocale = Locale.ITALY
        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24/04/10")
        assertThat(result).contains(ITALY_SHORT_TIME)
    }

    @Test fun ofLocalizedDateTime_italyLocaleMediumDateTimeFormat_usesMediumItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24 apr 2010")
        assertThat(result).contains(ITALY_MEDIUM_TIME)
    }

    @Test fun ofLocalizedDateTime_italyLocaleLongDateTimeFormat_usesLongItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG)
        assertEquals("24 aprile 2010 16:44:00 Z", formatter.format(DATE_TIME))
    }

    @Test fun ofLocalizedDateTime_italyLocaleFullDateTimeFormat_usesFullItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.FULL)
        assertEquals("sabato 24 aprile 2010 16:44:00 Z", formatter.format(DATE_TIME))
    }
    //endregion

    //region ofLocalizedDateTime with dateStyle and timeStyle
    @Test fun ofLocalizedDateTime_usLocaleShortDateFormatLongTimeFormat_usesShortDateLongTimeUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT, FormatStyle.LONG)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("4/24/10")
        assertThat(result).contains("4:44:00 PM Z")
    }

    @Test fun ofLocalizedDateTime_usLocaleMediumDateFormatShortTimeFormat_usesMediumDateShortTimeUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("Apr 24, 2010")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_usLocaleLongDateFormatFullTimeFormat_usesLongDateFullTimeUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.FULL)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("April 24, 2010")
        assertThat(result).contains("4:44:00 PM Z")
    }

    @Test fun ofLocalizedDateTime_usLocaleFullDateFormatMediumTimeFormat_usesFullDateMediumTimeUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.FULL, FormatStyle.MEDIUM)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("Saturday, April 24, 2010")
        assertThat(result).contains("4:44:00 PM")
    }

    @Test fun ofLocalizedDateTime_italyLocaleShortDateFormatFullTimeFormat_usesShortDateFullTimeItalyFormat() {
        testLocale = Locale.ITALY
        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT, FormatStyle.FULL)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24/04/10")
        assertThat(result).contains("16:44:00 Z")
    }

    @Test fun ofLocalizedDateTime_italyLocaleMediumDateFormatLongTimeFormat_usesMediumDateLongTimeItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.LONG)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24 apr 2010")
        assertThat(result).contains("16:44:00 Z")
    }

    @Test fun ofLocalizedDateTime_italyLocaleLongDateFormatMediumTimeFormat_usesLongDateMediumTimeItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.MEDIUM)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24 aprile 2010")
        assertThat(result).contains(ITALY_MEDIUM_TIME)
    }

    @Test fun ofLocalizedDateTime_italyLocaleFullDateFormatShortTimeFormat_usesFullDateShortTimeItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.FULL, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("sabato 24 aprile 2010")
        assertThat(result).contains(ITALY_SHORT_TIME)
    }
    //endregion

    private companion object {
        private val DATE = LocalDate.of(2010, Month.APRIL, 24)
        private val TIME = LocalTime.of(16, 44)
        private val DATE_TIME = ZonedDateTime.of(DATE, TIME, ZoneOffset.UTC)

        private val LEGACY_TIME: Date = TIME_FORMAT_24_IN_UTC.parse("16:44")!!

        private val ITALY_MEDIUM_TIME = if (Build.VERSION.SDK_INT > 25)
            "16:44:00"
        else
            "4:44:00 PM"
        private val ITALY_SHORT_TIME = ITALY_MEDIUM_TIME.replace(":00", "")
    }
}
