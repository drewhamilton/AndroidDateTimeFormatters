package drewhamilton.androiddatetimeformatters.test;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

public class SystemSettingsTestActivity extends Activity {

    public void requestWriteSettingsPermission() {
        startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
    }
}
