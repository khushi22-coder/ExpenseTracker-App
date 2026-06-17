package com.apexplanet.expensetracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.utils.UserPreferences;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView tvSkip;
    private LinearLayout dotsLayout;
    private OnboardingAdapter adapter;
    private UserPreferences userPreferences;

    private ImageView[] dots;
    private int totalSlides = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userPreferences = new UserPreferences(this);

        // Connect views
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        tvSkip = findViewById(R.id.tvSkip);
        dotsLayout = findViewById(R.id.dotsLayout);

        // Setup adapter
        adapter = new OnboardingAdapter();
        viewPager.setAdapter(adapter);

        // Setup dots
        setupDots(0);

        // ViewPager page change
        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        setupDots(position);

                        if (position == totalSlides - 1) {
                            btnNext.setText("GET STARTED");
                        } else {
                            btnNext.setText("NEXT");
                        }
                    }
                });

        // Next button click
        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < totalSlides - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                goToLogin();
            }
        });

        // Skip button click
        tvSkip.setOnClickListener(v -> goToLogin());
    }

    // Setup dot indicators
    private void setupDots(int currentPage) {
        dotsLayout.removeAllViews();
        dots = new ImageView[totalSlides];

        for (int i = 0; i < totalSlides; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(
                    i == currentPage
                            ? android.R.drawable.presence_online
                            : android.R.drawable.presence_invisible
            );

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
    }

    // Go to login screen
    private void goToLogin() {
        // Mark onboarding as seen
        userPreferences.setOnboardingSeen(true);

        Intent intent = new Intent(
                OnboardingActivity.this, LoginActivity.class
        );
        startActivity(intent);
        finish();
    }
}