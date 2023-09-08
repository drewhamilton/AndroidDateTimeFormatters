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
    @Test fun ofLocalizedTime_nullSystemSetting_matchesLegacySystemFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSetting_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSetting_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSetting_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(locale.shortTimePreferred, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSetting_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(locale.shortTime12, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSetting_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(locale.shortTime24, formattedTime)
    }
    //endregion

    //region ofLocalizedTime with explicit style
    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_matchesLegacySystemFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(expectedShortFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(locale.shortTimePreferred, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(locale.shortTime12, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertEquals(locale.shortTime24, formattedTime)
    }

    @Test fun ofLocalizedTime_mediumFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.MEDIUM)
        assertEquals(locale.mediumTime, formatter.format(time))
    }

    @Test fun ofLocalizedTime_longFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.LONG)
        assertEquals(locale.longTime, formatter.format(dateTime))
    }

    @Test fun ofLocalizedTime_fullFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.FULL)
        assertEquals(locale.fullTime, formatter.format(dateTime))
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
        private val preferredTimeSetting: String,
        val shortTime12: String,
        val shortTime24: String,
        val mediumTime: String,
        val longTime: String,
        val fullTime: String,
        val shortDate: String,
        val mediumDate: String,
        val longDate: String,
        val fullDate: String,
    ) {
        US(
            value = Locale.US,
            preferredTimeSetting = TIME_SETTING_12,
            shortTime12 = "6:01 PM",
            shortTime24 = "18:01",
            mediumTime = "6:01:00 PM",
            longTime = "6:01:00 PM CDT",
            fullTime = "6:01:00 PM Central Daylight Time",
            shortDate = "9/7/23",
            mediumDate = "Sep 7, 2023",
            longDate = "September 7, 2023",
            fullDate = "Thursday, September 7, 2023",
        ),
        Italy(
            value = Locale.ITALY,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = "6:01 PM",
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = "18:01:00 GMT-05:00",
            fullTime = "18:01:00 Ora legale centrale USA",
            shortDate = "07/09/23",
            mediumDate = "7 set 2023",
            longDate = "7 settembre 2023",
            fullDate = "giovedì 7 settembre 2023",
        ),
        ;

        val shortTimePreferred: String
            get() = when (preferredTimeSetting) {
                TIME_SETTING_12 -> shortTime12
                TIME_SETTING_24 -> shortTime24
                else -> throw AssertionError("Invalid preferred time setting: $preferredTimeSetting")
            }
    }
}
