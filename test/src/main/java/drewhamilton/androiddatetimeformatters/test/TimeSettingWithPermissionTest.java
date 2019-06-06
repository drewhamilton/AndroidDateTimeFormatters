package drewhamilton.androiddatetimeformatters.test;

import android.os.Build;
import android.provider.Settings;
import androidx.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

public abstract class TimeSettingWithPermissionTest extends TimeSettingTest {

    @Rule public final ActivityTestRule<SystemSettingsTestActivity> testActivityRule =
            new ActivityTestRule<>(SystemSettingsTestActivity.class);

    @Before
    public void getWriteSettingsPermission() {
        SystemSettingsTestActivity activity = getActivity();
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(activity)) {
            activity.requestWriteSettingsPermission();
            // TODO: Implement correct navigation
            onView(withId(android.R.id.checkbox)).perform(click());
            assertTrue(Settings.System.canWrite(activity));
        }
    }

    protected final SystemSettingsTestActivity getActivity() {
        return testActivityRule.getActivity();
    }
}
