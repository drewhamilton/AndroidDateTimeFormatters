package dev.drewhamilton.androidtime.format.demo

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

internal fun Context.extractPrimaryLocale(): Locale {
    val configuration = resources.configuration
    var locale: Locale? = null
    if (Build.VERSION.SDK_INT >= 24) {
        val localeList = configuration.locales
        if (!localeList.isEmpty) {
            locale = localeList[0]
        }
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

internal fun Context.copyWithLocale(locale: Locale): Context {
    val configuration = Configuration(resources.configuration)
    if (Build.VERSION.SDK_INT >= 24) {
        configuration.setLocales(LocaleList(locale))
    }
    @Suppress("DEPRECATION") // Fallback API < 24
    configuration.locale = locale
    return createConfigurationContext(configuration)
}
