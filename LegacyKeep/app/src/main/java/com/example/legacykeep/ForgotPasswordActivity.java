package com.example.legacykeep;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button sendResetButton;
    private TextView confirmationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.forgotEmailInput);
        sendResetButton = findViewById(R.id.sendResetButton);
        confirmationMessage = findViewById(R.id.resetConfirmationMessage);

        sendResetButton.setOnClickListener(v -> {
            // Simulate sending reset logic
            confirmationMessage.setVisibility(TextView.VISIBLE);
        });
    }
}
