package dev.drewhamilton.androidtime.format.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dev.drewhamilton.androidtime.format.AndroidDateTimeFormatter;
import dev.drewhamilton.androidtime.format.demo.databinding.DemoBinding;

public class Demo extends AppCompatActivity {

    private DemoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayJavaTime();
        displayThreeTenBpTime();
    }

    private void displayJavaTime() {
        DateTimeFormatter formatter = AndroidDateTimeFormatter.ofLocalizedTime(this);
        binding.javaTimeValue.setText(formatter.format(LocalTime.now()));
    }

    private void displayThreeTenBpTime() {
        org.threeten.bp.format.DateTimeFormatter formatter =
                dev.drewhamilton.androidtime.threetenbp.format.AndroidDateTimeFormatter.ofLocalizedTime(this);
        binding.threeTenBpValue.setText(formatter.format(org.threeten.bp.LocalTime.now()));
    }
}
