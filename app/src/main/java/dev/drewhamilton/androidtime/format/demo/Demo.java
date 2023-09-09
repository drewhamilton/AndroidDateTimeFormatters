package dev.drewhamilton.androidtime.format.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

        ZonedDateTime now = ZonedDateTime.now();

        DateTimeFormatter timeFormatter = AndroidDateTimeFormatter.ofLocalizedTime(this);
        binding.shortTimeValue.setText(timeFormatter.format(now));

        DateTimeFormatter dateTimeFormatter = AndroidDateTimeFormatter.ofLocalizedDateTime(this, FormatStyle.LONG, FormatStyle.SHORT);
        binding.dateTimeValue.setText(dateTimeFormatter.format(now));
    }
}
