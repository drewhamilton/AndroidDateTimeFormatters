package dev.drewhamilton.androiddatetimeformatters.demo;

import androidx.multidex.MultiDexApplication;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class DemoApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
