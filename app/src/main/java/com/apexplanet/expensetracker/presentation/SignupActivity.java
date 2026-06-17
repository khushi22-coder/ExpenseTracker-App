package com.apexplanet.expensetracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.utils.UserPreferences;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLogin;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userPreferences = new UserPreferences(this);

        // Connect views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        // Signup button click
        btnSignup.setOnClickListener(v -> signupUser());

        // Go to login
        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void signupUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Please enter your name");
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Please enter email");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter valid email");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Please enter password");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match!");
            return;
        }

        // Save user data
        userPreferences.setUserName(name);
        userPreferences.setUserEmail(email);
        userPreferences.setLoggedIn(true);

        Toast.makeText(this, "Account created successfully!",
                Toast.LENGTH_SHORT).show();

        // Go to MainActivity
        Intent intent = new Intent(SignupActivity.this,
                com.apexplanet.expensetracker.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}