package com.example.legacykeep;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Spinner roleSpinner, languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Connect layout to spinners
        roleSpinner = findViewById(R.id.roleSpinner);
        languageSpinner = findViewById(R.id.languageSpinner);

        // Load roles from strings.xml
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array,
                R.layout.spinner_item
        );
        roleAdapter.setDropDownViewResource(R.layout.spinner_item);
        roleSpinner.setAdapter(roleAdapter);

        // Load languages from strings.xml
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages_array,
                R.layout.spinner_item
        );
        languageAdapter.setDropDownViewResource(R.layout.spinner_item);
        languageSpinner.setAdapter(languageAdapter);
    }
}
