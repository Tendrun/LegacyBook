package com.legacybook.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.legacybook.Model.LoginRequest;
import com.legacybook.Model.LoginResponse;
import com.legacybook.Service.ApiService;
import com.legacybook.Service.ApiClient; // Add this if missing
import com.legacybook.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);

        findViewById(R.id.btnLogin).setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email.getText().toString(), password.getText().toString());

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
