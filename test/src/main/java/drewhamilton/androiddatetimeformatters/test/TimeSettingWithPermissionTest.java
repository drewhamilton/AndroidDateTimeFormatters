package drewhamilton.androiddatetimeformatters.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/**
 * A subclass of {@link TimeSettingTest} that runs in an activity and enables the
 * {@link android.Manifest.permission#WRITE_SETTINGS} permission before continuing to tests.
 * <p>
 * This class is incomplete. It is seemingly unnecessary as tests appear to be granted this permission without
 * requesting it.
 */
public abstract class TimeSettingWithPermissionTest extends TimeSettingTest {

    @Rule public final ActivityTestRule<Activity> testActivityRule = new ActivityTestRule<>(Activity.class);

    @Before
    public void getWriteSettingsPermission() {
        Activity activity = getActivity();
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(activity)) {
            activity.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
            // TODO: Implement correct navigation
            onView(withId(android.R.id.checkbox)).perform(click());
            assertTrue(Settings.System.canWrite(activity));
        }
    }

    protected final Activity getActivity() {
        return testActivityRule.getActivity();
    }
}
