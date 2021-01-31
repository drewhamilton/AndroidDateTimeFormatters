package dev.drewhamilton.androidtime.kotlinx

import android.content.Context
import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter
import java.time.format.FormatStyle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun testFormat(context: Context) {
    val localeDate = LocalDate(2021, Month.JANUARY, 31)
    val formatter = AndroidDateTimeFormatter.ofLocalizedDate(context, FormatStyle.LONG)
    formatter.format(localeDate)
}
