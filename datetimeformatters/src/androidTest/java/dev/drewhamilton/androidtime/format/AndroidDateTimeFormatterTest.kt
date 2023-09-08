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

    //region ofSkeleton
    @Test fun ofSkeleton_MMMMdJaLocaleFromContext_formatsToJapaneseMonthAndDay() {
        testLocale = Locale.JAPAN
        val formatter = AndroidDateTimeFormatter.ofSkeleton("MMMMd", testContext)
        assertThat(formatter.format(DATE)).isEqualTo("4月24日")
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
    }
}
