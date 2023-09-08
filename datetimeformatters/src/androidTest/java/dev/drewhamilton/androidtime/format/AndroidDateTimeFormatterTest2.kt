package dev.drewhamilton.androidtime.format

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import dev.drewhamilton.androidtime.format.test.TimeSettingTest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.FormatStyle
import java.util.Locale
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date as JavaUtilDate

@RunWith(TestParameterInjector::class)
class AndroidDateTimeFormatterTest2(
    @TestParameter val locale: TestLocale,
) : TimeSettingTest() {
    private val date: LocalDate = LocalDate.of(2023, Month.SEPTEMBER, 7)
    private val time: LocalTime = LocalTime.of(18, 1)
    private val dateTime: ZonedDateTime = ZonedDateTime.of(date, time, ZoneId.of("America/Chicago"))

    private val timeAsLegacyDate: JavaUtilDate = SimpleDateFormat("HH:mm", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse("18:01")!!

    private val expectedShortFormattedTime: String
        get() = androidShortTimeFormatInUtc.format(timeAsLegacyDate)

    //region ofLocalizedTime with default style
    @Test fun ofLocalizedTime_nullSystemSetting_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSetting_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSetting_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }
    //endregion

    //region ofLocalizedTime with explicit style
    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_mediumFormat_usesLocaleFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.MEDIUM)
        assertEquals(locale.mediumTime, formatter.format(time))
    }

    @Test fun ofLocalizedTime_longFormat_usesLocaleFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.LONG)
        assertEquals("${locale.mediumTime} ${locale.longTimeZone}", formatter.format(dateTime))
    }

    @Test fun ofLocalizedTime_fullFormat_usesLocaleFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.FULL)
        assertEquals("${locale.mediumTime} ${locale.fullTimeZone}", formatter.format(dateTime))
    }
    //endregion

    //region ofLocalizedDate
    @Test fun ofLocalizedDate_shortFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.SHORT)
        assertEquals(locale.shortDate, formatter.format(date))
    }

    @Test fun ofLocalizedDate_mediumFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.MEDIUM)
        assertEquals(locale.mediumDate, formatter.format(date))
    }

    @Test fun ofLocalizedDate_longFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.LONG)
        assertEquals(locale.longDate, formatter.format(date))
    }

    @Test fun ofLocalizedDate_fullFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.FULL)
        assertEquals(locale.fullDate, formatter.format(date))
    }
    //endregion

    @Suppress("unused")
    enum class TestLocale(
        val value: Locale,
        val mediumTime: String,
        val longTimeZone: String,
        val fullTimeZone: String,
        val shortDate: String,
        val mediumDate: String,
        val longDate: String,
        val fullDate: String,
    ) {
        US(
            value = Locale.US,
            mediumTime = "6:01:00 PM",
            longTimeZone = "CDT",
            fullTimeZone = "Central Daylight Time",
            shortDate = "9/7/23",
            mediumDate = "Sep 7, 2023",
            longDate = "September 7, 2023",
            fullDate = "Thursday, September 7, 2023",
        ),
        Italy(
            value = Locale.ITALY,
            mediumTime = "18:01:00",
            longTimeZone = "GMT-05:00",
            fullTimeZone = "Ora legale centrale USA",
            shortDate = "07/09/23",
            mediumDate = "7 set 2023",
            longDate = "7 settembre 2023",
            fullDate = "giovedì 7 settembre 2023",
        ),
    }
}
