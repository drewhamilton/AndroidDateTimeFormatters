package dev.drewhamilton.androidtime.format.test

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter
import java.util.Locale
import org.junit.Before

/**
 * A base test class that facilitates using and changing [testSystemTimeSetting], which mimics the
 * [Settings.System.TIME_12_24] setting, without actually affecting the test device's setting.
 */
abstract class TimeSettingTest {

    protected val testContext: Context
        get() = InstrumentationRegistry.getInstrumentation().context

    protected var localeContext: Context = testContext
        private set

    protected var testLocale: Locale
        get() = ConfigurationCompat.getLocales(localeContext.resources.configuration)[0]!!
        set(value) {
            localeContext = testContext.copyWithLocale(value)
        }

    protected var systemTimeSetting: String?
        get() = AndroidDateTimeFormatter.testSystemTimeSetting
        set(value) {
            AndroidDateTimeFormatter.testSystemTimeSetting = value
        }

    private fun Context.copyWithLocale(locale: Locale): Context {
        val configuration = Configuration(resources.configuration).apply {
            setLocales(LocaleListCompat.create(locale))
        }
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

    @Before fun enableTestSystemTimeSetting() {
        AndroidDateTimeFormatter.useTestSystemTimeSetting = true
    }

    private var AndroidDateTimeFormatter.useTestSystemTimeSetting: Boolean
        get() = javaClass.declaredFields
            .single { it.name == "useTestSystemTimeSetting" }
            .apply { isAccessible = true }
            .get(this) as Boolean
        set(value) {
            javaClass.declaredFields
                .single { it.name == "useTestSystemTimeSetting" }
                .apply { isAccessible = true }
                .set(this, value)
        }

    private var AndroidDateTimeFormatter.testSystemTimeSetting: String?
        get() = javaClass.declaredFields
            .single { it.name == "testSystemTimeSetting" }
            .apply { isAccessible = true }
            .get(this) as String?
        set(value) {
            javaClass.declaredFields
                .single { it.name == "testSystemTimeSetting" }
                .apply { isAccessible = true }
                .set(this, value)
        }

    protected companion object {
        const val TIME_SETTING_12 = "12"
        const val TIME_SETTING_24 = "24"
    }
}
