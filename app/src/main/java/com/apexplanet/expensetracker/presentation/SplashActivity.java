package com.apexplanet.expensetracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.apexplanet.expensetracker.MainActivity;
import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.utils.UserPreferences;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        UserPreferences userPreferences = new UserPreferences(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Intent intent;

            if (userPreferences.isLoggedIn()) {
                // Already logged in - go to main
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else if (userPreferences.isOnboardingSeen()) {
                // Onboarding seen - go to login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            } else {
                // First time - show onboarding
                intent = new Intent(SplashActivity.this,
                        OnboardingActivity.class);
            }

            startActivity(intent);
            finish();

        }, SPLASH_DURATION);
    }
}