package com.apexplanet.expensetracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.utils.UserPreferences;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail;
    private TextView tvTotalTransactions;
    private TextView tvProfileIncome, tvProfileExpense;
    private Button btnLogout;
    private ExpenseViewModel viewModel;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile 👤");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userPreferences = new UserPreferences(this);
        viewModel = new ViewModelProvider(this)
                .get(ExpenseViewModel.class);

        // Connect views
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvTotalTransactions = findViewById(R.id.tvTotalTransactions);
        tvProfileIncome = findViewById(R.id.tvProfileIncome);
        tvProfileExpense = findViewById(R.id.tvProfileExpense);
        btnLogout = findViewById(R.id.btnLogout);

        // Set user info
        tvProfileName.setText(userPreferences.getUserName());
        tvProfileEmail.setText(userPreferences.getUserEmail());

        // Observe data
        viewModel.getAllExpenses().observe(this, expenses -> {
            tvTotalTransactions.setText(
                    String.valueOf(expenses.size())
            );
        });

        viewModel.getTotalIncome().observe(this, income -> {
            tvProfileIncome.setText("₹" +
                    String.format("%.2f", income != null ?
                            income : 0));
        });

        viewModel.getTotalExpense().observe(this, expense -> {
            tvProfileExpense.setText("₹" +
                    String.format("%.2f", expense != null ?
                            expense : 0));
        });

        // Logout button
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    userPreferences.logout();
                    Intent intent = new Intent(
                            ProfileActivity.this,
                            LoginActivity.class
                    );
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}