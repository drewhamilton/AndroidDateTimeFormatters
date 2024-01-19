package dev.drewhamilton.androidtime.format

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
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
import org.junit.Assume.assumeFalse
import org.junit.Assume.assumeTrue
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date as JavaUtilDate

@RunWith(TestParameterInjector::class)
class AndroidDateTimeFormatterTest(
    @TestParameter val locale: TestLocale,
) : TimeSettingTest() {

    private val date: LocalDate = LocalDate.of(2023, Month.SEPTEMBER, 7)
    private val time: LocalTime = LocalTime.of(18, 1)
    private val dateTime: ZonedDateTime = ZonedDateTime.of(date, time, ZoneId.of("America/Chicago"))

    private val timeAsLegacyDate: JavaUtilDate = SimpleDateFormat("HH:mm", locale.value).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse("18:01")!!

    //region ofLocalizedTime with default style
    @Test fun ofLocalizedTime_nullSystemSetting_matchesLegacySystemFormat() {
        assumeNullableSystemTimeSetting()
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_12SystemSetting_matchesLegacySystemFormat() {
        assumeShortTime12ShouldMatchLegacySystemFormat()
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_24SystemSetting_matchesLegacySystemFormat() {
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_nullSystemSetting_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

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
    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_matchesLegacySystemFormat() {
        assumeNullableSystemTimeSetting()
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_12SystemSettingShortFormat_matchesLegacySystemFormat() {
        assumeShortTime12ShouldMatchLegacySystemFormat()
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_24SystemSettingShortFormat_matchesLegacySystemFormat() {
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(localeContext, FormatStyle.SHORT)
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_nullSystemSettingShortFormat_usesLocaleFormat() {
        assumeNullableSystemTimeSetting()

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
    @Test fun ofLocalizedTime_nullSystemSettingLocaleAndShortFormat_matchesLegacySystemFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)
        assumeNullableSystemTimeSetting()
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = testLocale,
            timeStyle = FormatStyle.SHORT,
        )
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_12SystemSettingLocaleAndShortFormat_matchesLegacySystemFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)
        assumeShortTime12ShouldMatchLegacySystemFormat()
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = testLocale,
            timeStyle = FormatStyle.SHORT,
        )
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_24SystemSettingLocaleAndShortFormat_matchesLegacySystemFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)
        assumeShortTimeShouldMatchLegacySystemFormat()

        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = testLocale,
            timeStyle = FormatStyle.SHORT,
        )
        val formattedTime = formatter.format(time)
        assertThat(formattedTime).isEqualTo(expectedShortFormattedTime(localeContext))
    }

    @Test fun ofLocalizedTime_nullSystemSettingLocaleAndShortFormat_usesLocaleFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)
        assumeNullableSystemTimeSetting()

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
        assumeTrue(Build.VERSION.SDK_INT >= 17)

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
        assumeTrue(Build.VERSION.SDK_INT >= 17)

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
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.MEDIUM,
        )
        assertThat(formatter.format(time)).isEqualTo(locale.mediumTime)
    }

    @Test fun ofLocalizedTime_localeAndLongFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(
            context = testContext,
            locale = locale.value,
            timeStyle = FormatStyle.LONG,
        )
        assertThat(formatter.format(dateTime)).isEqualTo(locale.longTime)
    }

    @Test fun ofLocalizedTime_localeAndFullFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

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
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SystemSettingShortDateTimeFormat_usesShort12HourFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SystemSettingShortDateTimeFormat_usesShort24HourFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_mediumDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.MEDIUM)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $mediumTime")
        }
    }

    @Test fun ofLocalizedDateTime_longDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.LONG)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $longTime")
        }
    }

    @Test fun ofLocalizedDateTime_fullDateTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.FULL)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate $fullTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with explicit locale and dateTimeStyle
    @Test fun ofLocalizedDateTime_nullSystemSettingLocaleAndShortDateTimeFormat_usesShortLocaleFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SystemSettingLocaleAndShortDateTimeFormat_usesShort12HourFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        systemTimeSetting = TIME_SETTING_12

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SystemSettingLocaleAndShortDateTimeFormat_usesShort24HourFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndMediumDateTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.MEDIUM,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $mediumTime")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndLongDateTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.LONG,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $longTime")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndFullDateTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateTimeStyle = FormatStyle.FULL,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate $fullTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with dateStyle and timeStyle
    @Test fun ofLocalizedDateTime_shortDateFormatLongTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.SHORT, FormatStyle.LONG)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $longTime")
        }
    }

    @Test fun ofLocalizedDateTime_nullSettingLongDateFormatShortTimeFormat_usesLongDateLocaleTimeFormat() {
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.LONG, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SettingMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeFormat() {
        systemTimeSetting = TIME_SETTING_12
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SettingMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeFormat() {
        systemTimeSetting = TIME_SETTING_24
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.MEDIUM, FormatStyle.SHORT)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_longDateFormatFullTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.LONG, FormatStyle.FULL)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $fullTime")
        }
    }

    @Test fun ofLocalizedDateTime_fullDateFormatMediumTimeFormat() {
        testLocale = locale.value

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(localeContext, FormatStyle.FULL, FormatStyle.MEDIUM)

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate $mediumTime")
        }
    }
    //endregion

    //region ofLocalizedDateTime with explicit locale, dateStyle, and timeStyle
    @Test fun ofLocalizedDateTime_localeAndShortDateFormatLongTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.SHORT,
            timeStyle = FormatStyle.LONG,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$shortDate $longTime")
        }
    }

    @Test fun ofLocalizedDateTime_nullSettingLocaleAndLongDateFormatShortTimeFormat_usesLongDateLocaleTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)
        assumeNullableSystemTimeSetting()

        systemTimeSetting = null

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.LONG,
            timeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $shortTimePreferred")
        }
    }

    @Test fun ofLocalizedDateTime_12SettingLocaleAndMediumDateFormatShortTimeFormat_usesMediumDate12HourTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        systemTimeSetting = TIME_SETTING_12

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.MEDIUM,
            timeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $shortTime12")
        }
    }

    @Test fun ofLocalizedDateTime_24SettingLocaleAndMediumDateFormatShortTimeFormat_usesMediumDate24HourTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        systemTimeSetting = TIME_SETTING_24

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.MEDIUM,
            timeStyle = FormatStyle.SHORT,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$mediumDate $shortTime24")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndLongDateFormatFullTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.LONG,
            timeStyle = FormatStyle.FULL,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$longDate $fullTime")
        }
    }

    @Test fun ofLocalizedDateTime_localeAndFullDateFormatMediumTimeFormat() {
        assumeTrue(Build.VERSION.SDK_INT >= 17)

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateTime(
            context = testContext,
            locale = locale.value,
            dateStyle = FormatStyle.FULL,
            timeStyle = FormatStyle.MEDIUM,
        )

        val result = formatter.format(dateTime)
        with(locale) {
            assertThat(result).isEqualTo("$fullDate $mediumTime")
        }
    }
    //endregion

    //region ofSkeleton
    @Test fun ofSkeleton_MMMMdAndContext() {
        assumeTrue(Build.VERSION.SDK_INT >= 18)
        // FIXME: API 29 on GH Actions sometimes prints Persian "October" instead of "September"
        assumeFalse(Build.VERSION.SDK_INT == 29 && locale == TestLocale.Persian)

        testLocale = locale.value
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", localeContext)
        assertThat(formatter.format(date)).isEqualTo(locale.skeletonMMMMd)
    }

    @Test fun ofSkeleton_MMMMdAndLocale() {
        assumeTrue(Build.VERSION.SDK_INT >= 18)
        // FIXME: API 29 on GH Actions sometimes prints Persian "October" instead of "September"
        assumeFalse(Build.VERSION.SDK_INT == 29 && locale == TestLocale.Persian)

        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", locale.value)
        assertThat(formatter.format(date)).isEqualTo(locale.skeletonMMMMd)
    }
    //endregion

    private fun expectedShortFormattedTime(context: Context): String {
        return DateFormat.getTimeFormat(context).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(timeAsLegacyDate)
    }

    /**
     * On older APIs, the desugar libs do better than the system format did for some locales.
     */
    private fun assumeShortTime12ShouldMatchLegacySystemFormat() = assumeFalse(
        Build.VERSION.SDK_INT < 28 && locale in setOf(
            TestLocale.CanadaFrench,
            TestLocale.Japan,
            TestLocale.Russian,
            TestLocale.Persian,
        )
    )

    /**
     * On newer APIs, the desugar libs don't use alternate digits, while the legacy formatter does.
     */
    private fun assumeShortTimeShouldMatchLegacySystemFormat() = assumeFalse(
        Build.VERSION.SDK_INT >= 28 && locale == TestLocale.Persian
    )

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
            skeletonMMMMd = "September 7"
        ),
        Italy(
            value = Locale.ITALY,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT>= 23 -> "6:01 PM"
                else -> "06:01 PM"
            },
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = when {
                Build.VERSION.SDK_INT >= 21 -> "18:01:00 GMT-05:00"
                else -> "18:01:00 CDT"
            },
            fullTime = "18:01:00 Ora legale centrale USA",
            shortDate = "07/09/23",
            mediumDate = when {
                Build.VERSION.SDK_INT >= 28 -> "7 set 2023"
                Build.VERSION.SDK_INT >= 23 -> "07 set 2023"
                else -> "07/set/2023"
            },
            longDate = when {
                Build.VERSION.SDK_INT >= 23 -> "7 settembre 2023"
                else -> "07 settembre 2023"
            },
            fullDate = "giovedì 7 settembre 2023",
            skeletonMMMMd = "7 settembre",
        ),
        France(
            value = Locale.FRANCE,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = "6:01 PM",
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = when {
                Build.VERSION.SDK_INT >= 21 -> "18:01:00 GMT-05:00"
                else -> "18:01:00 CDT"
            },
            fullTime = when {
                Build.VERSION.SDK_INT >= 23 -> "18:01:00 heure d’été du Centre"
                else -> "18:01:00 heure avancée du Centre"
            },
            shortDate = "07/09/2023",
            mediumDate = "7 sept. 2023",
            longDate = "7 septembre 2023",
            fullDate = "jeudi 7 septembre 2023",
            skeletonMMMMd = "7 septembre",
        ),
        CanadaFrench(
            value = Locale.CANADA_FRENCH,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 28 -> "6 h 01 p.m."
                Build.VERSION.SDK_INT >= 26 -> "6:01 p.m."
                else -> "6:01 PM"
            },
            shortTime24 = when {
                Build.VERSION.SDK_INT >= 28 -> "18 h 01"
                else -> "18:01"
            },
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
                Build.VERSION.SDK_INT >= 23 -> "18:01:00 heure avancée du Centre"
                else -> "18 h 01 min 00 s heure avancée du Centre"
            },
            shortDate = when {
                Build.VERSION.SDK_INT >= 30 -> "2023-09-07"
                else -> "23-09-07"
            },
            mediumDate = when {
                Build.VERSION.SDK_INT >= 23 -> "7 sept. 2023"
                else -> "2023-09-07"
            },
            longDate = "7 septembre 2023",
            fullDate = "jeudi 7 septembre 2023",
            skeletonMMMMd = "7 septembre",
        ),
        Japan(
            value = Locale.JAPAN,
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = "午後6:01",
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = when {
                Build.VERSION.SDK_INT >= 21 -> "18:01:00 GMT-05:00"
                else -> "18:01:00 CDT"
            },
            fullTime = "18時01分00秒 アメリカ中部夏時間",
            shortDate = "2023/09/07",
            mediumDate = "2023/09/07",
            longDate = "2023年9月7日",
            fullDate = "2023年9月7日木曜日",
            skeletonMMMMd = "9月7日",
        ),
        Russian(
            value = Locale("ru"),
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = when {
                Build.VERSION.SDK_INT >= 28 -> "6:01 PM"
                Build.VERSION.SDK_INT >= 24 -> "6:01 ПП"
                Build.VERSION.SDK_INT >= 21 -> "6:01 PM"
                else -> "6:01 после полудня"
            },
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = when {
                Build.VERSION.SDK_INT >= 21 -> "18:01:00 GMT-05:00"
                else -> "18:01:00 CDT"
            },
            fullTime = when {
                Build.VERSION.SDK_INT >= 21 -> "18:01:00 Центральная Америка, летнее время"
                else -> "18:01:00 Средне-американское летнее время"
            },
            shortDate = when {
                Build.VERSION.SDK_INT >= 26 -> "07.09.2023"
                else -> "07.09.23"
            },
            mediumDate = when {
                Build.VERSION.SDK_INT >= 21 -> "7 сент. 2023 г."
                else -> "07 сент. 2023 г."
            },
            longDate = "7 сентября 2023 г.",
            fullDate = "четверг, 7 сентября 2023 г.",
            skeletonMMMMd = "7 сентября",
        ),
        Persian(
            value = Locale("fa"),
            preferredTimeSetting = TIME_SETTING_24,
            shortTime12 = "6:01 بعدازظهر",
            shortTime24 = "18:01",
            mediumTime = "18:01:00",
            longTime = when {
                Build.VERSION.SDK_INT >= 21 -> "18:01:00 (GMT-05:00)"
                else -> "18:01:00 (CDT)"
            },
            fullTime = "18:01:00 (وقت تابستانی مرکز امریکا)",
            shortDate = "2023/9/7",
            mediumDate = "7 سپتامبر 2023",
            longDate = "7 سپتامبر 2023",
            fullDate = "پنجشنبه 7 سپتامبر 2023",
            skeletonMMMMd = "7 سپتامبر"
        ),
        ;

        val shortTimePreferred: String
            get() = when (preferredTimeSetting) {
                TIME_SETTING_12 -> shortTime12
                TIME_SETTING_24 -> shortTime24
                else -> throw AssertionError("Invalid preferred time setting: $preferredTimeSetting")
            }
    }

    companion object {
        /**
         * This class has strange behavior on APIs 22 through 27, inconsistently using the wrong
         * 12/24 setting on short and/or medium times despite editing the system setting.
         * Preemptively fail on these APIs to avoid confusion.
         */
        @BeforeClass
        @JvmStatic fun preventApis22Through27() {
            assertThat(Build.VERSION.SDK_INT).isNotIn(22..27)
        }
    }
}
