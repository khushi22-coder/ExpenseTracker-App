package com.apexplanet.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apexplanet.expensetracker.data.Expense;
import com.apexplanet.expensetracker.presentation.AddExpenseActivity;
import com.apexplanet.expensetracker.presentation.ExpenseAdapter;
import com.apexplanet.expensetracker.presentation.ExpenseViewModel;
import com.apexplanet.expensetracker.presentation.LoginActivity;
import com.apexplanet.expensetracker.utils.UserPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ExpenseViewModel viewModel;
    private ExpenseAdapter adapter;
    private UserPreferences userPreferences;

    private TextView tvBalance, tvIncome, tvExpense, tvNoTransaction;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPreferences = new UserPreferences(this);

        // Set title with username
        String userName = userPreferences.getUserName();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hi, " + userName + " 👋");
        }

        // Connect views
        tvBalance = findViewById(R.id.tvBalance);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);
        tvNoTransaction = findViewById(R.id.tvNoTransaction);
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        // Setup RecyclerView
        adapter = new ExpenseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this)
                .get(ExpenseViewModel.class);

        // Observe expenses list
        viewModel.getAllExpenses().observe(this, expenses -> {
            adapter.setExpenses(expenses);

            if (expenses.isEmpty()) {
                tvNoTransaction.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvNoTransaction.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Observe total income
        viewModel.getTotalIncome().observe(this, income -> {
            if (income != null) {
                tvIncome.setText("₹" + String.format("%.2f", income));
            } else {
                tvIncome.setText("₹0.00");
            }
            updateBalance();
        });

        // Observe total expense
        viewModel.getTotalExpense().observe(this, expense -> {
            if (expense != null) {
                tvExpense.setText("₹" + String.format("%.2f", expense));
            } else {
                tvExpense.setText("₹0.00");
            }
            updateBalance();
        });

        // FAB click
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this, AddExpenseActivity.class
            );
            startActivity(intent);
        });

        // Long click to delete
        adapter.setOnItemClickListener(
                new ExpenseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Expense expense) {
                        // do nothing
                    }

                    @Override
                    public void onItemLongClick(Expense expense) {
                        showDeleteDialog(expense);
                    }
                });
    }

    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_theme) {
            toggleTheme();
            return true;
        } else if (id == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Toggle Dark/Light theme
    private void toggleTheme() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
            );
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
            );
        }
    }

    // Show logout confirmation
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    userPreferences.logout();
                    Intent intent = new Intent(
                            MainActivity.this, LoginActivity.class
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

    // Calculate balance
    private void updateBalance() {
        String incomeStr = tvIncome.getText().toString()
                .replace("₹", "");
        String expenseStr = tvExpense.getText().toString()
                .replace("₹", "");

        try {
            double income = Double.parseDouble(incomeStr);
            double expense = Double.parseDouble(expenseStr);
            double balance = income - expense;
            tvBalance.setText(
                    "₹" + String.format("%.2f", balance)
            );
        } catch (NumberFormatException e) {
            tvBalance.setText("₹0.00");
        }
    }

    // Delete dialog
    private void showDeleteDialog(Expense expense) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete '"
                        + expense.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.delete(expense);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}