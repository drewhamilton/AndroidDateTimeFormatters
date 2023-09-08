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
    @Test fun ofSkeleton_MMMMdJaLocaleFromContext_formatsToJapaneseMonthAndDay() {
        testLocale = Locale.JAPAN
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", testContext)
        assertThat(formatter.format(DATE)).isEqualTo("4月24日")
    }

    @Test fun ofSkeleton_MMMMdUsLocale_formatsToFullMonthFollowedByDay() {
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", Locale.US)
        assertThat(formatter.format(DATE)).isEqualTo("April 24")
    }

    @Test fun ofSkeleton_MMMMdRuLocale_formatsToDayFollowedByRussianMonth() {
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", Locale("ru"))
        assertThat(formatter.format(DATE)).isEqualTo("24 апреля")
    }

    // Unwanted case because coreLibraryDesugaring does not support "L" format:
    @Test fun ofSkeleton_MMMMdFaLocale_formatsToDayFollowedByMonthNumber() {
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", Locale("fa"))
        assertThat(formatter.format(DATE)).isEqualTo("24 4")
    }
    //endregion

    private companion object {
        private val DATE = LocalDate.of(2010, Month.APRIL, 24)
        private val TIME = LocalTime.of(16, 44)
        private val DATE_TIME = ZonedDateTime.of(DATE, TIME, ZoneOffset.UTC)

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
    }
}
