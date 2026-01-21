package dev.drewhamilton.androidtime.format.demo

import android.content.Context
import android.os.Build
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

internal fun parseLocaleString(value: String): Locale? {
    val parts = value.split('_')
    return when (parts.size) {
        0 -> null
        1 -> localeOf(language = parts.single())
        2 -> localeOf(language = parts[0], country = parts[1])
        3 -> localeOf(language = parts[0], country = parts[1], variant = parts[2])
        else -> null
    }?.let {
        if (it.toLanguageTag() == "und") {
            null
        } else {
            it
        }
    }
}

private fun localeOf(language: String): Locale {
    return if (Build.VERSION.SDK_INT >= 36) {
        Locale.of(language)
    } else {
        @Suppress("DEPRECATION")
        Locale(language)
    }
}

private fun localeOf(language: String, country: String): Locale {
    return if (Build.VERSION.SDK_INT >= 36) {
        Locale.of(language, country)
    } else {
        @Suppress("DEPRECATION")
        Locale(language, country)
    }
}

private fun localeOf(language: String, country: String, variant: String): Locale {
    return if (Build.VERSION.SDK_INT >= 36) {
        Locale.of(language, country, variant)
    } else {
        @Suppress("DEPRECATION")
        Locale(language, country, variant)
    }
}
