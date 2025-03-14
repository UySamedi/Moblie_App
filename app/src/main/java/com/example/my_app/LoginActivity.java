package com.example.my_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_app.MyApi.ApiInterface;
import com.example.my_app.MyApi.ApiResponse;
import com.example.my_app.MyApi.LoginRequest;
import com.example.my_app.MyApi.RetrofitInstace;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView textRegister, textForgotPassword;
    private ImageView iconEye;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        textRegister = findViewById(R.id.text_register);
        textForgotPassword = findViewById(R.id.text_forgot_password);
        iconEye = findViewById(R.id.icon_eye);

        // Set initial password visibility state (hidden)
        editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        iconEye.setImageResource(R.drawable.eye_svgrepo_com); // Closed eye icon

        // Toggle password visibility on eye icon click
        iconEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iconEye.setImageResource(R.drawable.eye_svgrepo_com); // Closed eye icon
                } else {
                    // Show password
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iconEye.setImageResource(R.drawable.eye_off_svgrepo_com); // Open eye icon
                }
                isPasswordVisible = !isPasswordVisible; // Toggle state
                editPassword.setSelection(editPassword.getText().length()); // Move cursor to the end
            }
        });

        // Handle login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        // Handle register text click
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Handle forgot password text click
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ForgotPasswordActivity
                Toast.makeText(LoginActivity.this, "Navigate to Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to hide the keyboard
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Hide keyboard when clicking outside input fields
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null) {
                hideKeyboard();
                view.clearFocus(); // Clear focus from the input field
            }
        }
        return super.onTouchEvent(event);
    }

    private void loginUser(String email, String password) {
        ApiInterface apiInterface = RetrofitInstace.getInstance().getApiInterface();
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<ApiResponse> call = apiInterface.login(loginRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    // Navigate to HomeActivity on successful login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Optional: Call finish() to close the LoginActivity
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Error: ", t);
            }
        });
    }
}