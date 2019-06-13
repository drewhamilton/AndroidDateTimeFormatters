package drewhamilton.androiddatetimeformatters.test

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule

/**
 * A subclass of [TimeSettingTest] that runs in an activity and enables the [android.Manifest.permission.WRITE_SETTINGS]
 * permission before continuing to tests.
 *
 * This class is incomplete. It is seemingly unnecessary as tests appear to be granted this permission without
 * requesting it.
 */
abstract class TimeSettingWithPermissionTest : TimeSettingTest() {

    @Rule val testActivityRule = ActivityTestRule(Activity::class.java)

    @Before
    fun getWriteSettingsPermission() {
        val activity = activity
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(activity)) {
            activity.startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS))
            // TODO: Implement correct navigation
            onView(withId(android.R.id.checkbox)).perform(click())
            assertTrue(Settings.System.canWrite(activity))
        }
    }

    protected val activity get(): Activity = testActivityRule.activity
}
