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
import com.apexplanet.expensetracker.utils.BiometricHelper;
import com.apexplanet.expensetracker.utils.UserPreferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private UserPreferences userPreferences;
    private BiometricHelper biometricHelper;
    private ExpenseDatabase database;
    private ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userPreferences = new UserPreferences(this);
        biometricHelper = new BiometricHelper(this);
        database = ExpenseDatabase.getInstance(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        // Show biometric if available
        if (biometricHelper.isBiometricAvailable()
                && !userPreferences.getUserEmail().isEmpty()) {
            showBiometricLogin();
        }

        btnLogin.setOnClickListener(v -> loginUser());
        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(
                    LoginActivity.this, SignupActivity.class
            ));
        });
    }

    private void showBiometricLogin() {
        biometricHelper.showBiometricPrompt(
                new BiometricHelper.BiometricCallback() {
                    @Override
                    public void onSuccess() {
                        userPreferences.setLoggedIn(true);
                        Toast.makeText(LoginActivity.this,
                                "Fingerprint login successful!",
                                Toast.LENGTH_SHORT).show();
                        goToMain();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(LoginActivity.this,
                                "Fingerprint not recognized!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        // User cancelled - use password
                    }
                });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
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

        // Show loading
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        // Check credentials in database
        executorService.execute(() -> {
            User user = database.userDao().login(email, password);

            runOnUiThread(() -> {
                if (user != null) {
                    // Login successful
                    userPreferences.setLoggedIn(true);
                    userPreferences.setUserName(user.getName());
                    userPreferences.setUserEmail(user.getEmail());

                    Toast.makeText(LoginActivity.this,
                            "Welcome back " + user.getName() + "!",
                            Toast.LENGTH_SHORT).show();
                    goToMain();
                } else {
                    // Login failed
                    btnLogin.setEnabled(true);
                    btnLogin.setText("LOGIN");
                    Toast.makeText(LoginActivity.this,
                            "Invalid email or password!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void goToMain() {
        Intent intent = new Intent(
                LoginActivity.this, MainActivity.class
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}