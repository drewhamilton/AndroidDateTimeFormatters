package drewhamilton.androiddatetimeformatters.demo;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
