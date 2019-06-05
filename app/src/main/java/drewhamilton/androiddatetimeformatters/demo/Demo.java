package drewhamilton.androiddatetimeformatters.demo;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import drewhamilton.androiddatetimeformatters.javatime.AndroidDateTimeFormatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Demo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 26) {
            displayJavaTime();
        }
        displayThreeTenBpTime();
    }

    @RequiresApi(26)
    private void displayJavaTime() {
        TextView javaTimeValue = findViewById(R.id.javaTimeValue);
        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(getApplicationContext());
        javaTimeValue.setText(formatter.format(LocalTime.now()));
    }

    private void displayThreeTenBpTime() {
        TextView threeTenBpValue = findViewById(R.id.threeTenBpValue);
        org.threeten.bp.format.DateTimeFormatter formatter =
                drewhamilton.androiddatetimeformatters.threetenbp.AndroidDateTimeFormatter
                        .ofLocalizedTime(getApplicationContext());
        threeTenBpValue.setText(formatter.format(org.threeten.bp.LocalTime.now()));
    }
}
