package dev.drewhamilton.androidtime.format

import android.os.Build
import com.google.common.truth.Truth.assertThat
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import dev.drewhamilton.androidtime.format.test.TimeSettingTest
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.FormatStyle
import java.util.Locale
import org.junit.Assume.assumeFalse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class AndroidDateTimeFormatterTest(
    @param:TestParameter val locale: TestLocale,
) : TimeSettingTest() {

    private val date: LocalDate = LocalDate.of(2023, Month.SEPTEMBER, 7)
    private val time: LocalTime = LocalTime.of(18, 1)
    private val dateTime: ZonedDateTime = ZonedDateTime.of(date, time, ZoneId.of("America/Chicago"))

    //region ofLocalizedTime with default style
    @Test fun ofLocalizedTime_nullSystemSetting_usesLocaleFormat() {
        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTimePreferred)
    }

    @Test fun ofLocalizedTime_12SystemSetting_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime12)
    }

    @Test fun ofLocalizedTime_24SystemSetting_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime24)
    }
    //endregion

    //region ofLocalizedTime with explicit style
    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_usesLocaleFormat() {
        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTimePreferred)
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime12)
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime24)
    }

    @Test fun ofLocalizedTime_mediumFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.MEDIUM)
        assertThat(formatter.format(time)).isEqualTo(locale.mediumTime)
    }

    @Test fun ofLocalizedTime_longFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.LONG)
        assertThat(formatter.format(dateTime)).isEqualTo(locale.longTime)
    }

    @Test fun ofLocalizedTime_fullFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.FULL)
        assertThat(formatter.format(dateTime)).isEqualTo(locale.fullTime)
    }
    //endregion

    //region ofLocalizedTime with explicit locale and style
    @Test fun ofLocalizedTime_nullSystemSettingLocaleAndShortFormat_usesLocaleFormat() {
        systemTimeSetting = null

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.SHORT,
        )
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTimePreferred)
    }

    @Test fun ofLocalizedTime_12SystemSettingLocaleAndShortFormat_uses12HourFormat() {
        systemTimeSetting = TIME_SETTING_12

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.SHORT,
        )
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime12)
    }

    @Test fun ofLocalizedTime_24SystemSettingLocaleAndShortFormat_uses24HourFormat() {
        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.SHORT,
        )
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(locale.shortTime24)
    }

    @Test fun ofLocalizedTime_localeAndMediumFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.MEDIUM,
        )
        assertThat(formatter.format(time)).isEqualTo(locale.mediumTime)
    }

    @Test fun ofLocalizedTime_localeAndLongFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.LONG,
        )
        assertThat(formatter.format(dateTime)).isEqualTo(locale.longTime)
    }

    @Test fun ofLocalizedTime_localeAndFullFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.FULL,
        )
        assertThat(formatter.format(dateTime)).isEqualTo(locale.fullTime)
    }
    //endregion

    //region ofLocalizedDate
    @Test fun ofLocalizedDate_shortFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(localeContext, FormatStyle.SHORT)
        assertThat(formatter.format(date)).isEqualTo(locale.shortDate)
    }

    @Test fun ofLocalizedDate_mediumFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(localeContext, FormatStyle.MEDIUM)
        assertThat(formatter.format(date)).isEqualTo(locale.mediumDate)
    }

    @Test fun ofLocalizedDate_longFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(localeContext, FormatStyle.LONG)
        assertThat(formatter.format(date)).isEqualTo(locale.longDate)
    }

    @Test fun ofLocalizedDate_fullFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(localeContext, FormatStyle.FULL)
        assertThat(formatter.format(date)).isEqualTo(locale.fullDate)
    }
    //endregion

    //region ofLocalizedDate with explicit locale
    @Test fun ofLocalizedDate_localeAndShortFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(locale.value, FormatStyle.SHORT)
        assertThat(formatter.format(date)).isEqualTo(locale.shortDate)
    }

    @Test fun ofLocalizedDate_localeAndMediumFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(locale.value, FormatStyle.MEDIUM)
        assertThat(formatter.format(date)).isEqualTo(locale.mediumDate)
    }

    @Test fun ofLocalizedDate_localeAndLongFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(locale.value, FormatStyle.LONG)
        assertThat(formatter.format(date)).isEqualTo(locale.longDate)
    }

    @Test fun ofLocalizedDate_localeAndFullFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDate(locale.value, FormatStyle.FULL)
        assertThat(formatter.format(date)).isEqualTo(locale.fullDate)
    }
    //endregion

    //region ofLocalizedDateTime with dateTimeStyle
    @Test fun ofLocalizedDateTime_nullSystemSettingShortDateTimeFormat_usesShortLocaleFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SystemSettingShortDateTimeFormat_usesShort12HourFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SystemSettingShortDateTimeFormat_usesShort24HourFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_mediumDateTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 31 && locale == TestLocale.France)

        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.MEDIUM)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate$mediumDateTimeJoiner$mediumTime")
        }
    }

    @Test fun ofLocalizedDateTime_longDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.LONG)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate$longDateTimeJoiner$longTime")
        }
    }

    @Test fun ofLocalizedDateTime_fullDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.FULL)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate$longDateTimeJoiner$fullTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with explicit locale and dateTimeStyle
    @Test fun ofLocalizedDateTime_nullSystemSettingLocaleAndShortDateTimeFormat_usesShortLocaleFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        systemTimeSetting = null

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SystemSettingLocaleAndShortDateTimeFormat_usesShort12HourFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        systemTimeSetting = TIME_SETTING_12

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SystemSettingLocaleAndShortDateTimeFormat_usesShort24HourFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndMediumDateTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 31 && locale == TestLocale.France)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.MEDIUM,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate$mediumDateTimeJoiner$mediumTime")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndLongDateTimeFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.LONG,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate$longDateTimeJoiner$longTime")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndFullDateTimeFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.FULL,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate$longDateTimeJoiner$fullTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with dateStyle and timeStyle
    @Test fun ofLocalizedDateTime_shortDateFormatLongTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT, FormatStyle.LONG)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$longTime")
        }
    }

    @Test fun ofLocalizedDateTime_nullSettingLongDateFormatShortTimeFormat_usesLongDateLocaleTimeFormat() {
        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.LONG, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate$longDateTimeJoiner$shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SettingMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 31 && locale == TestLocale.France)

        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate$mediumDateTimeJoiner$shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SettingMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 31 && locale == TestLocale.France)

        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate$mediumDateTimeJoiner$shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_longDateFormatFullTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.LONG, FormatStyle.FULL)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate$longDateTimeJoiner$fullTime")
        }
    }

    @Test fun ofLocalizedDateTime_fullDateFormatMediumTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.FULL, FormatStyle.MEDIUM)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate$longDateTimeJoiner$mediumTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with explicit locale, dateStyle, and timeStyle
    @Test fun ofLocalizedDateTime_localeAndShortDateFormatLongTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 34 && locale == TestLocale.Persian)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.SHORT,
            timeStyle = FormatStyle.LONG,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate$shortDateTimeJoiner$longTime")
        }
    }

    @Test fun ofLocalizedDateTime_nullSettingLocaleAndLongDateFormatShortTimeFormat_usesLongDateLocaleTimeFormat() {
        systemTimeSetting = null

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.LONG,
            timeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate$longDateTimeJoiner$shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SettingLocaleAndMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 31 && locale == TestLocale.France)

        systemTimeSetting = TIME_SETTING_12

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.MEDIUM,
            timeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate$mediumDateTimeJoiner$shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SettingLocaleAndMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeFormat() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT < 31 && locale == TestLocale.France)

        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.MEDIUM,
            timeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate$mediumDateTimeJoiner$shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndLongDateFormatFullTimeFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.LONG,
            timeStyle = FormatStyle.FULL,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate$longDateTimeJoiner$fullTime")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndFullDateFormatMediumTimeFormat() {
        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.FULL,
            timeStyle = FormatStyle.MEDIUM,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate$longDateTimeJoiner$mediumTime")
        }
    }
    //endregion

    //region ofSkeleton
    @Test fun ofSkeleton_MMMMdAndContext() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT == 29 && locale == TestLocale.Persian)

        testLocale = locale.value
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", localeContext)
        assertThat(formatter.format(date)).isEqualTo(locale.skeletonMMMMd)
    }

    @Test fun ofSkeleton_MMMMdAndLocale() {
        // Behavior is inconsistent on older OS levels:
        assumeFalse(Build.VERSION.SDK_INT == 29 && locale == TestLocale.Persian)

        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", locale.value)
        assertThat(formatter.format(date)).isEqualTo(locale.skeletonMMMMd)
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
        val skeletonMMMMd: String,
        val dateTimeJoiner: String = " ",
        val shortDateTimeJoiner: String = dateTimeJoiner,
        val mediumDateTimeJoiner: String = dateTimeJoiner,
        val longDateTimeJoiner: String = dateTimeJoiner,
    ) {
        US(
            value = Locale.US,
            preferredTimeSetting = TIME_SETTING_12,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01\u202fPM"
                else -> "6:01 PM"
            },
            shortTime24 = "18:01",
            mediumTime = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01:00\u202fPM"
                else -> "6:01:00 PM"
            },
            longTime = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01:00\u202fPM CDT"
                else -> "6:01:00 PM CDT"
            },
            fullTime = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01:00\u202fPM Central Daylight Time"
                else -> "6:01:00 PM Central Daylight Time"
            },
            shortDate = "9/7/23",
            mediumDate = "Sep 7, 2023",
            longDate = "September 7, 2023",
            fullDate = "Thursday, September 7, 2023",
            skeletonMMMMd = "September 7",
            dateTimeJoiner = ", ",
            longDateTimeJoiner = " at ",
        ),
        Italy(
            value = Locale.ITALY,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01\u202fPM"
                Build.VERSION.SDK_INT >= 23 -> "6:01 PM"
                else -> "06:01 PM"
            },
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = "18:01:00 GMT-05:00",
            fullTime = "18:01:00 Ora legale centrale USA",
            shortDate = "07/09/23",
            mediumDate = when {
                Build.VERSION.SDK_INT >= 28 -> "7 set 2023"
                else -> "07 set 2023"
            },
            longDate = "7 settembre 2023",
            fullDate = "giovedì 7 settembre 2023",
            skeletonMMMMd = "7 settembre",
            dateTimeJoiner = ", ",
            longDateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 34 -> " alle ore "
                else -> " "
            },
        ),
        France(
            value = Locale.FRANCE,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01\u202fPM"
                else -> "6:01 PM"
            },
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = "18:01:00 GMT-05:00",
            fullTime = when {
                Build.VERSION.SDK_INT >= 34 -> "18:01:00 heure d’été du centre nord-américain"
                else -> "18:01:00 heure d’été du Centre"
            },
            shortDate = "07/09/2023",
            mediumDate = "7 sept. 2023",
            longDate = "7 septembre 2023",
            fullDate = "jeudi 7 septembre 2023",
            skeletonMMMMd = "7 septembre",
            dateTimeJoiner = " à ",
            shortDateTimeJoiner = " ",
            mediumDateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 31 -> ", "
                else -> " "
            },
        ),
        CanadaFrench(
            value = Locale.CANADA_FRENCH,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 34 -> "6 h 01\u202fp.m."
                Build.VERSION.SDK_INT >= 28 -> "6 h 01 p.m."
                else -> "6:01 p.m."
            },
            shortTime24 = "18 h 01",
            mediumTime = when {
                Build.VERSION.SDK_INT >= 28 -> "18 h 01 min 00 s"
                else -> "18:01:00"
            },
            longTime = when {
                Build.VERSION.SDK_INT >= 28 -> "18 h 01 min 00 s HAC"
                else -> "18:01:00 HAC"
            },
            fullTime = when {
                Build.VERSION.SDK_INT >= 28 -> "18 h 01 min 00 s heure avancée du Centre"
                else -> "18:01:00 heure avancée du Centre"
            },
            shortDate = when {
                Build.VERSION.SDK_INT >= 30 -> "2023-09-07"
                else -> "23-09-07"
            },
            mediumDate = "7 sept. 2023",
            longDate = "7 septembre 2023",
            fullDate = "jeudi 7 septembre 2023",
            skeletonMMMMd = "7 septembre",
            mediumDateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 31 -> ", "
                else -> " "
            },
            longDateTimeJoiner = " à ",
        ),
        Japan(
            value = Locale.JAPAN,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = "午後6:01",
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = "18:01:00 GMT-05:00",
            fullTime = "18時01分00秒 アメリカ中部夏時間",
            shortDate = "2023/09/07",
            mediumDate = "2023/09/07",
            longDate = "2023年9月7日",
            fullDate = "2023年9月7日木曜日",
            skeletonMMMMd = "9月7日",
        ),
        Russian(
            value = localeOf(language = "ru"),
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 34 -> "6:01\u202fPM"
                Build.VERSION.SDK_INT >= 28 -> "6:01 PM"
                else -> "6:01 ПП"
            },
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = "18:01:00 GMT-05:00",
            fullTime = "18:01:00 Центральная Америка, летнее время",
            shortDate = "07.09.2023",
            mediumDate = when {
                Build.VERSION.SDK_INT >= 34 -> "7 сент. 2023\u202fг."
                else -> "7 сент. 2023 г."
            },
            longDate = when {
                Build.VERSION.SDK_INT >= 34 -> "7 сентября 2023\u202fг."
                else -> "7 сентября 2023 г."
            },
            fullDate = when {
                Build.VERSION.SDK_INT >= 34 -> "четверг, 7 сентября 2023\u202fг."
                else -> "четверг, 7 сентября 2023 г."
            },
            skeletonMMMMd = "7 сентября",
            dateTimeJoiner = ", ",
            longDateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 34 -> " в "
                else -> ", "
            }
        ),
        Persian(
            value = localeOf(language = "fa"),
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = "6:01 بعدازظهر",
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = "18:01:00 (GMT-05:00)",
            fullTime = "18:01:00 (وقت تابستانی مرکز امریکا)",
            shortDate = "2023/9/7",
            mediumDate = "7 سپتامبر 2023",
            longDate = "7 سپتامبر 2023",
            fullDate = "پنجشنبه 7 سپتامبر 2023",
            skeletonMMMMd = "7 سپتامبر",
            dateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 34 -> "، "
                else -> "،\u200F "
            },
            shortDateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 34 -> ", "
                else -> "، "
            },
            longDateTimeJoiner = when {
                Build.VERSION.SDK_INT >= 34 -> " ساعت "
                else -> "، ساعت "
            },
        ),
        ;

        val shortTimePreferred: String
            get() = when (preferredTimeSetting) {
                TIME_SETTING_12 -> shortTime12
                TIME_SETTING_24 -> shortTime24
                else -> throw AssertionError("Invalid preferred time setting: $preferredTimeSetting")
            }
    }

    private companion object {
        fun localeOf(language: String): Locale = if (Build.VERSION.SDK_INT < 36) {
            @Suppress("DEPRECATION")
            Locale(language)
        } else {
            Locale.of(language)
        }
    }
}
