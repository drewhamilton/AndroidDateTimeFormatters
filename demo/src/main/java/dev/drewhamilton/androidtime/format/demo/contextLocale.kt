package dev.drewhamilton.androidtime.format.demo

import android.content.Context
import java.util.Locale

internal fun Context.extractPrimaryLocale(): Locale {
    val configuration = resources.configuration
    var locale: Locale? = null
    val localeList = configuration.locales
    if (!localeList.isEmpty) {
        locale = localeList[0]
    }
    if (locale == null) {
        @Suppress("DEPRECATION") // Fallback for API < 24
        locale = configuration.locale
    }
    if (locale == null) {
        locale = Locale.getDefault()
    }
    return locale!!
}
