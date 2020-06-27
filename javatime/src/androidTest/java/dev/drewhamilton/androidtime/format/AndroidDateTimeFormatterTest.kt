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

    private val expectedShortFormattedTime: String
        get() = androidShortTimeFormatInUtc.format(LEGACY_TIME)

    //region ofLocalizedTime with default style
    @Test fun ofLocalizedTime_nullSystemSettingUsLocale_uses12HourFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingItalyLocale_uses24HourFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }
    //endregion

    //region ofLocalizedTime with explicit style
    @Test fun ofLocalizedTime_nullSystemSettingUsLocaleShortFormat_uses12HourFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocaleShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocaleShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_usLocaleMediumFormat_usesMediumUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.MEDIUM)
        assertEquals("4:44:00 PM", formatter.format(TIME))
    }

    @Test fun ofLocalizedTime_usLocaleLongFormat_usesLongUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.LONG)
        assertEquals("4:44:00 PM Z", formatter.format(DATE_TIME))
    }

    @Test fun ofLocalizedTime_usLocaleFullFormat_usesFullUsFormat() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.FULL)
        assertEquals("4:44:00 PM Z", formatter.format(DATE_TIME))
    }

    @Test fun ofLocalizedTime_nullSystemSettingItalyLocaleShortFormat_uses24HourFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocaleShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocaleShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_italyLocaleMediumFormat_usesMediumItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.MEDIUM)
        assertEquals(ITALY_MEDIUM_TIME, formatter.format(TIME))
    }

    @Test fun ofLocalizedTime_italyLocaleLongFormat_usesLongItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.LONG)
        assertEquals("16:44:00 Z", formatter.format(DATE_TIME))
    }

    @Test fun ofLocalizedTime_italyLocaleFullFormat_usesFullItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.FULL)
        assertEquals("16:44:00 Z", formatter.format(DATE_TIME))
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
        assertEquals(ITALY_MEDIUM_DATE, formatter.format(DATE))
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
    @Test fun ofLocalizedDateTime_nullSystemSettingUsLocaleShortDateTimeFormat_usesShort12HourUsFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("4/24/10")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_12SystemSettingUsLocaleShortDateTimeFormat_usesShort12HourUsFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("4/24/10")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_24SystemSettingUsLocaleShortDateTimeFormat_usesShort24HourUsFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("4/24/10")
        assertThat(result).contains("16:44")
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

    @Test fun ofLocalizedDateTime_nullSystemSettingItalyLocaleShortDateTimeFormat_usesShort24HourItalyFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24/04/10")
        assertThat(result).contains("16:44")
    }

    @Test fun ofLocalizedDateTime_12SystemSettingItalyLocaleShortDateTimeFormat_usesShort12HourItalyFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24/04/10")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_24SystemSettingItalyLocaleShortDateTimeFormat_usesShort24HourItalyFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24/04/10")
        assertThat(result).contains("16:44")
    }

    @Test fun ofLocalizedDateTime_italyLocaleMediumDateTimeFormat_usesMediumItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains(ITALY_MEDIUM_DATE)
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

    @Test fun ofLocalizedDateTime_nullSettingUsLocaleLongDateFormatShortTimeFormat_usesLongDate12HourTimeUsFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("April 24, 2010")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_12SettingUsLocaleMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeUsFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("Apr 24, 2010")
        assertThat(result).contains("4:44 PM")
    }

    @Test fun ofLocalizedDateTime_24SettingUsLocaleMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeUsFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("Apr 24, 2010")
        assertThat(result).contains("16:44")
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
        assertThat(result).contains(ITALY_MEDIUM_DATE)
        assertThat(result).contains("16:44:00 Z")
    }

    @Test fun ofLocalizedDateTime_italyLocaleLongDateFormatMediumTimeFormat_usesLongDateMediumTimeItalyFormat() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.MEDIUM)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24 aprile 2010")
        assertThat(result).contains(ITALY_MEDIUM_TIME)
    }

    @Test
    fun ofLocalizedDateTime_nullSettingItalyLocaleLongDateFormatShortTimeFormat_usesLongDate24HourTimeItalyFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains("24 aprile 2010")
        assertThat(result).contains("16:44")
    }

    @Test
    fun ofLocalizedDateTime_12SettingItalyLocaleMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeItalyFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains(ITALY_MEDIUM_DATE)
        assertThat(result).contains("4:44 PM")
    }

    @Test
    fun ofLocalizedDateTime_24SettingItalyLocaleMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeItalyFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(DATE_TIME)
        assertThat(result).contains(ITALY_MEDIUM_DATE)
        assertThat(result).contains("16:44")
    }
    //endregion

    //region ofSkeleton
    @Test fun ofSkeleton_MMMMdUsLocale_formatsToFullMonthFollowedByDay() {
        val formatter = AndroidDateTimeFormatter.ofSkeleton(Locale.US, "MMMMd")
        assertThat(formatter.format(JANUARY_4_2020)).isEqualTo("January 4")
    }

    @Test fun ofSkeleton_MMMMdRuLocale_formatsToDayFollowedByRussianMonth() {
        val formatter = AndroidDateTimeFormatter.ofSkeleton(Locale.forLanguageTag("ru"), "MMMMd")
        assertThat(formatter.format(JANUARY_4_2020)).isEqualTo("4 января")
    }

    // Unwanted case because coreLibraryDesugaring does not support "L" format:
    @Test fun ofSkeleton_MMMMdFaLocale_formatsToDayFollowedByMonthNumber() {
        val formatter = AndroidDateTimeFormatter.ofSkeleton(Locale.forLanguageTag("fa"), "MMMMd")
        assertThat(formatter.format(JANUARY_4_2020)).isEqualTo("4 1")
    }
    //endregion

    private fun assumeNullableSystemTimeSetting() = assumeFalse(
        "Time setting is not nullable in API ${Build.VERSION.SDK_INT}",
        Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING
    )

    private companion object {
        private val DATE = LocalDate.of(2010, Month.APRIL, 24)
        private val TIME = LocalTime.of(16, 44)
        private val DATE_TIME = ZonedDateTime.of(DATE, TIME, ZoneOffset.UTC)

        private val LEGACY_TIME: Date = TIME_FORMAT_24_IN_UTC.parse("16:44")!!

        private val ITALY_MEDIUM_DATE = if (Build.VERSION.SDK_INT > 22)
            "24 apr 2010"
        else
            "24/apr/2010"

        private val ITALY_MEDIUM_TIME = when {
            Build.VERSION.SDK_INT > 25 -> "16:44:00"
            Build.VERSION.SDK_INT > 22 -> "4:44:00 PM"
            Build.VERSION.SDK_INT > 21 -> "04:44:00 PM"
            else -> "16:44:00"
        }

        private val JANUARY_4_2020 = LocalDate.of(2020, Month.JANUARY, 4)
    }
}
