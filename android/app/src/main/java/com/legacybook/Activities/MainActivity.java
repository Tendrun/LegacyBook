package com.legacybook.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        TextView textView = new TextView(this);
        textView.setText("Hello, LegacyBook!");
        textView.setTextSize(24);

        layout.addView(textView);
        setContentView(layout);
    }
}
