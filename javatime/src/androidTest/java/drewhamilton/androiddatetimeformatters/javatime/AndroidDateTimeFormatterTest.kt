package drewhamilton.androiddatetimeformatters.javatime

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import drewhamilton.androiddatetimeformatters.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@RequiresApi(26)
class AndroidDateTimeFormatterTest : TimeSettingTest() {

    @Test fun ofLocalizedTime_nullSystemSettingUsLocale_uses12HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API ${Build.VERSION.SDK_INT}")
            return
        }

        timeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        timeSetting = "12"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        timeSetting = "24"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingItalyLocale_uses24HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API ${Build.VERSION.SDK_INT}")
            return
        }

        timeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        timeSetting = "12"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        timeSetting = "24"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_usLocale_formatsShortDateCorrectly() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateShort(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedDateShort, formattedTime)
    }

    @Test fun ofLocalizedTime_usLocale_formatsMediumDateCorrectly() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateMedium(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedDateMedium, formattedTime)
    }

    @Test fun ofLocalizedTime_usLocale_formatsLongDateCorrectly() {
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateLong(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedDateLong, formattedTime)
    }

    @Test fun ofLocalizedTime_italyLocale_formatsShortDateCorrectly() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateShort(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedDateShort, formattedTime)
    }

    @Test fun ofLocalizedTime_italyLocale_formatsMediumDateCorrectly() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateMedium(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedDateMedium, formattedTime)
    }

    @Test fun ofLocalizedTime_italyLocale_formatsLongDateCorrectly() {
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedDateLong(testContext)
        val formattedTime = formatter.format(DATETIME)
        assertEquals(expectedFormattedDateLong, formattedTime)
    }

    private val expectedFormattedTime get(): String = androidFormatTimeInUtc.format(LEGACY_DATE_TIME)
    private val expectedFormattedDateShort get(): String = androidFormatDateShort.format(LEGACY_DATE_TIME)
    private val expectedFormattedDateMedium get(): String = androidFormatDateMedium.format(LEGACY_DATE_TIME)
    private val expectedFormattedDateLong get(): String = androidFormatDateLong.format(LEGACY_DATE_TIME)

    private companion object {
        private val TAG = AndroidDateTimeFormatterTest::class.java.simpleName

        private const val SDK_INT_NULLABLE_TIME_SETTING = 28

        private val DATETIME = LocalDateTime.of(2012, 3, 4, 16, 44)
        @JvmStatic private val LEGACY_DATE_TIME: Date = dateTimeFormatInUtc.parse("2012-03-04_16:44")
    }
}
