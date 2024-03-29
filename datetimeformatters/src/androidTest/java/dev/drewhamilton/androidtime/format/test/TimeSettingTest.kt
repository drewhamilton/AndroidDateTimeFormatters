package dev.drewhamilton.androidtime.format.test

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import android.util.Log
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import org.junit.After
import org.junit.Assume.assumeFalse
import org.junit.Before

/**
 * A base test class that facilitates using and changing the [Settings.System.TIME_12_24] setting by caching the current
 * setting before each test and resetting it after.
 *
 * Currently it appears that even though changing this setting requires the [android.Manifest.permission.WRITE_SETTINGS]
 * permission, tests work just fine without explicitly requesting it as long as the permission is declared in the test
 * manifest.
 *
 * Warning: on APIs 23-27, `null` is the default system time setting. But _setting_ `null` as the system time setting is
 * unsupported. Thus, it is impossible to reset the device to its original system time setting of null. As a fallback,
 * the system time setting is reset to the 12/24 hour preference of the device's primary locale instead.
 */
abstract class TimeSettingTest {

    protected val testContext: Context
        get() = InstrumentationRegistry.getInstrumentation().context

    protected var localeContext: Context = testContext

    protected var testLocale: Locale
        get() = ConfigurationCompat.getLocales(localeContext.resources.configuration)[0]!!
        set(value) {
            localeContext = testContext.copyWithLocale(value)
        }

    protected var systemTimeSetting: String?
        get() = Settings.System.getString(testContext.contentResolver, Settings.System.TIME_12_24)
        set(value) {
            Settings.System.putString(testContext.contentResolver, Settings.System.TIME_12_24, value)
        }

    private lateinit var originalLocales: LocaleListCompat
    private var originalTimeSetting: String? = null
    private var canResetSystemTimeSetting: Boolean = false

    private fun Context.copyWithLocale(locale: Locale): Context {
        val configuration = Configuration(resources.configuration)
        configuration.setLocales(LocaleListCompat.create(locale))
        return createConfigurationContext(configuration)
    }

    private fun Configuration.setLocales(locales: LocaleListCompat) = when {
        Build.VERSION.SDK_INT >= 24 ->
            setLocales(locales.unwrap() as LocaleList)
        Build.VERSION.SDK_INT >= 17 ->
            setLocale(locales[0])
        else ->
            @Suppress("DEPRECATION")
            locale = locales[0]
    }

    @Before fun cacheOriginalTimeSetting() {
        originalTimeSetting = systemTimeSetting
        try {
            forceRestoreSystemTimeSetting()
            canResetSystemTimeSetting = true
            Log.d(TAG, "Cached original device time setting: $originalTimeSetting")
        } catch (illegalArgumentException: IllegalArgumentException) {
            canResetSystemTimeSetting = false
            Log.w(TAG, "Cannot restore original device time setting; will fall back to setting from primary Locale")
        }
    }

    @After fun restoreTimeSetting() {
        if (canResetSystemTimeSetting) {
            forceRestoreSystemTimeSetting()
            Log.d(TAG, "Reset device time setting to original setting: $originalTimeSetting")
        } else {
            val newTimeSetting = if (originalLocales[0]!!.is24HourLocale())
                TIME_SETTING_24
            else
                TIME_SETTING_12
            systemTimeSetting = newTimeSetting
            Log.d(TAG, "Reset device time setting to: $newTimeSetting")
        }
    }

    private fun forceRestoreSystemTimeSetting() {
        systemTimeSetting = originalTimeSetting
    }

    private fun Locale.is24HourLocale(): Boolean {
        val natural = DateFormat.getTimeInstance(DateFormat.LONG, this)
        return if (natural is SimpleDateFormat)
            natural.toPattern().hasDesignator('H')
        else
            false
    }

    private fun CharSequence?.hasDesignator(designator: Char): Boolean {
        if (this == null)
            return false

        var insideQuote = false
        forEach { c ->
            if (c == '\'') {
                insideQuote = !insideQuote
            } else if (!insideQuote) {
                if (c == designator)
                    return true
            }
        }

        return false
    }

    protected fun assumeNullableSystemTimeSetting() = assumeFalse(
        "Time setting is not nullable in API ${Build.VERSION.SDK_INT}",
        Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING
    )

    protected companion object {
        private val TAG = TimeSettingTest::class.java.simpleName

        const val SDK_INT_NULLABLE_TIME_SETTING = 28

        const val TIME_SETTING_12 = "12"
        const val TIME_SETTING_24 = "24"
    }
}
