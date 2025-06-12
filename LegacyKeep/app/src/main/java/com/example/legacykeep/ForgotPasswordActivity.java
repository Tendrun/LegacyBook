package com.example.legacykeep;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button sendResetButton;
    private TextView confirmationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.forgotEmailInput);
        EditText newPasswordInput = findViewById(R.id.newPasswordInput);
        sendResetButton = findViewById(R.id.sendResetButton);
        confirmationMessage = findViewById(R.id.resetConfirmationMessage);

        sendResetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String newPassword = newPasswordInput.getText().toString().trim();

            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email, newPassword);
        });
    }

    private void resetPassword(String email, String newPassword) {
        ApiService apiService = ApiClient.getApiService();
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("newPassword", newPassword);

        Call<String> call = apiService.resetPassword(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    confirmationMessage.setVisibility(View.VISIBLE);
                    confirmationMessage.setText("Password reset successfully!");
                } else {
                    try {
                        int statusCode = response.code();
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        System.out.println("Error response: " + errorBody);
                        System.out.println("HTTP Status Code: " + statusCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
