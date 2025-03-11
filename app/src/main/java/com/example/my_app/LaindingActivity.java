package com.example.my_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class LaindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lainding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Get references to views
        MaterialButton btnGetStarted = findViewById(R.id.btnGetStarted);
        TextView tvSignInLink = findViewById(R.id.tvSignInLink);

        // Set up listener for "Get Started" button to go to RegisterActivity
        btnGetStarted.setOnClickListener(view -> {
            Intent intent = new Intent(LaindingActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up listener for "Sign in" text to go to LoginActivity
        tvSignInLink.setOnClickListener(view -> {
            Intent intent = new Intent(LaindingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}