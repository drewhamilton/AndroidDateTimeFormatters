package drewhamilton.androiddatetimeformatters.test;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import androidx.annotation.RequiresApi;

public class SystemSettingsTestActivity extends Activity {

    @RequiresApi(23)
    public void requestWriteSettingsPermission() {
        startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
    }
}
