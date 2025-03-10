package com.example.my_app;

import android.app.ProgressDialog;
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
import com.example.my_app.MyApi.RegisterRequest;
import com.example.my_app.MyApi.RetrofitInstace;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName, editEmailRegister, editPasswordRegister, editConfirmPasswordRegister;
    private Button btnRegister;
    private TextView textLogin;
    private ImageView iconEye, iconEye2;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        initViews();

        // Set up register button click listener
        btnRegister.setOnClickListener(v -> validateAndRegister());

        // Set up login text click listener
        textLogin.setOnClickListener(v -> navigateToLogin());

        // Set up password visibility toggle
        setupPasswordVisibilityToggle();
    }

    private void initViews() {
        editName = findViewById(R.id.edit_name);
        editEmailRegister = findViewById(R.id.edit_email_register);
        editPasswordRegister = findViewById(R.id.edit_password_register);
        editConfirmPasswordRegister = findViewById(R.id.edit_confirm_password_register);
        btnRegister = findViewById(R.id.btn_register);
        textLogin = findViewById(R.id.text_register);
        iconEye = findViewById(R.id.icon_eye);
        iconEye2 = findViewById(R.id.icon_eye2);
    }

    private void setupPasswordVisibilityToggle() {
        // Set initial password visibility state
        editPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
        iconEye.setImageResource(R.drawable.eye_svgrepo_com); // Closed eye icon

        // Toggle password visibility on eye icon click
        iconEye.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Hide password
                editPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                iconEye.setImageResource(R.drawable.eye_svgrepo_com); // Closed eye icon
            } else {
                // Show password
                editPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                iconEye.setImageResource(R.drawable.eye_off_svgrepo_com); // Open eye icon
            }
            isPasswordVisible = !isPasswordVisible; // Toggle state
            editPasswordRegister.setSelection(editPasswordRegister.getText().length()); // Move cursor to the end
        });

        // Set initial confirm password visibility state
        editConfirmPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
        iconEye2.setImageResource(R.drawable.eye_svgrepo_com); // Closed eye icon

        // Toggle confirm password visibility on eye icon click
        iconEye2.setOnClickListener(v -> {
            if (isConfirmPasswordVisible) {
                // Hide confirm password
                editConfirmPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                iconEye2.setImageResource(R.drawable.eye_svgrepo_com); // Closed eye icon
            } else {
                // Show confirm password
                editConfirmPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                iconEye2.setImageResource(R.drawable.eye_off_svgrepo_com); // Open eye icon
            }
            isConfirmPasswordVisible = !isConfirmPasswordVisible; // Toggle state
            editConfirmPasswordRegister.setSelection(editConfirmPasswordRegister.getText().length()); // Move cursor to the end
        });
    }

    private void validateAndRegister() {
        String name = editName.getText().toString().trim();
        String email = editEmailRegister.getText().toString().trim();
        String password = editPasswordRegister.getText().toString().trim();
        String confirmPassword = editConfirmPasswordRegister.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
        } else if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(name, email, password, confirmPassword);
        }
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8; // Example: Minimum 8 characters
    }

    private void registerUser(String name, String email, String password, String confirmPassword) {
        showProgressDialog();

        ApiInterface apiInterface = RetrofitInstace.getInstance().getApiInterface();
        RegisterRequest registerRequest = new RegisterRequest(name, email, password, confirmPassword);

        Call<ApiResponse> call = apiInterface.register(registerRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Toast.makeText(RegisterActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("RegisterActivity", "Error: ", t);
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleErrorResponse(Response<ApiResponse> response) {
        if (response.code() == 400) {
            Toast.makeText(this, "Bad request: Invalid input", Toast.LENGTH_SHORT).show();
        } else if (response.code() == 500) {
            Toast.makeText(this, "Server error: Please try again later", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null) {
                hideKeyboard();
                view.clearFocus();
            }
        }
        return super.onTouchEvent(event);
    }
}