package dev.drewhamilton.androidtime.format

import com.google.common.truth.Truth.assertThat
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

    private val timeAsLegacyDate: JavaUtilDate = SimpleDateFormat("HH:mm", locale.value).apply {
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
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSetting_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSetting_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSetting_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTimePreferred)
    }

    @Test fun ofLocalizedTime_12SystemSetting_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime12)
    }

    @Test fun ofLocalizedTime_24SystemSetting_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime24)
    }
    //endregion

    //region ofLocalizedTime with explicit style
    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_matchesLegacySystemFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_matchesLegacySystemFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTimePreferred)
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime12)
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime24)
    }

    @Test fun ofLocalizedTime_mediumFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.MEDIUM)
        assertThat(formatter.format(time)).isEqualTo(locale.mediumTime)
    }

    @Test fun ofLocalizedTime_longFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.LONG)
        assertThat(formatter.format(dateTime)).isEqualTo(locale.longTime)
    }

    @Test fun ofLocalizedTime_fullFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext, FormatStyle.FULL)
        assertThat(formatter.format(dateTime)).isEqualTo(locale.fullTime)
    }
    //endregion

    //region ofLocalizedDate
    @Test fun ofLocalizedDate_shortFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.SHORT)
        assertThat(formatter.format(date)).isEqualTo(locale.shortDate)
    }

    @Test fun ofLocalizedDate_mediumFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.MEDIUM)
        assertThat(formatter.format(date)).isEqualTo(locale.mediumDate)
    }

    @Test fun ofLocalizedDate_longFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.LONG)
        assertThat(formatter.format(date)).isEqualTo(locale.longDate)
    }

    @Test fun ofLocalizedDate_fullFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(testContext, FormatStyle.FULL)
        assertThat(formatter.format(date)).isEqualTo(locale.fullDate)
    }
    //endregion

    //region ofLocalizedDateTime with dateTimeStyle
    @Test fun ofLocalizedDateTime_nullSystemSettingShortDateTimeFormat_usesShortLocaleFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SystemSettingShortDateTimeFormat_usesShort12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SystemSettingShortDateTimeFormat_usesShort24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_mediumDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $mediumTime")
        }
    }

    @Test fun ofLocalizedDateTime_longDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $longTime")
        }
    }

    @Test fun ofLocalizedDateTime_fullDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.FULL)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate $fullTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with dateStyle and timeStyle
    @Test fun ofLocalizedDateTime_shortDateFormatLongTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.SHORT, FormatStyle.LONG)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $longTime")
        }
    }

    @Test fun ofLocalizedDateTime_nullSettingLongDateFormatShortTimeFormat_usesLongDateLocaleTimeFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SettingMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SettingMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_longDateFormatFullTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.LONG, FormatStyle.FULL)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $fullTime")
        }
    }

    @Test fun ofLocalizedDateTime_fullDateFormatMediumTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(testContext, FormatStyle.FULL, FormatStyle.MEDIUM)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate $mediumTime")
        }
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