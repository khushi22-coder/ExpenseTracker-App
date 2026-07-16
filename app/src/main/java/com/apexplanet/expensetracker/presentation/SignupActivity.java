package com.apexplanet.expensetracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apexplanet.expensetracker.MainActivity;
import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.data.ExpenseDatabase;
import com.apexplanet.expensetracker.data.User;
import com.apexplanet.expensetracker.utils.UserPreferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLogin;
    private UserPreferences userPreferences;
    private ExpenseDatabase database;
    private ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userPreferences = new UserPreferences(this);
        database = ExpenseDatabase.getInstance(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(v -> signupUser());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void signupUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword
                .getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Please enter your name");
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Please enter email");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {
            etEmail.setError("Please enter valid email");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Please enter password");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Min 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match!");
            return;
        }

        // Show loading
        btnSignup.setEnabled(false);
        btnSignup.setText("Creating account...");

        // Check if email already exists in database
        executorService.execute(() -> {
            int emailExists = database.userDao()
                    .checkEmailExists(email);

            runOnUiThread(() -> {
                if (emailExists > 0) {
                    etEmail.setError("Email already registered!");
                    btnSignup.setEnabled(true);
                    btnSignup.setText("CREATE ACCOUNT");
                } else {
                    // Save user to database
                    saveUser(name, email, password);
                }
            });
        });
    }

    private void saveUser(String name, String email,
                          String password) {
        executorService.execute(() -> {
            // Save to Room Database
            User user = new User(name, email, password);
            database.userDao().insert(user);

            // Save to preferences for quick access
            userPreferences.setLoggedIn(true);
            userPreferences.setUserName(name);
            userPreferences.setUserEmail(email);

            runOnUiThread(() -> {
                Toast.makeText(SignupActivity.this,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        SignupActivity.this,
                        MainActivity.class
                );
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                startActivity(intent);
                finish();
            });
        });
    }
}