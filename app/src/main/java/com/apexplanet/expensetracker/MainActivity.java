package com.apexplanet.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.apexplanet.expensetracker.presentation.ProfileActivity;
import com.apexplanet.expensetracker.presentation.StatisticsActivity;
import com.apexplanet.expensetracker.utils.UserPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpenseViewModel viewModel;
    private ExpenseAdapter adapter;
    private UserPreferences userPreferences;
    private List<Expense> allExpensesList = new ArrayList<>();

    private TextView tvBalance, tvIncome, tvExpense, tvNoTransaction;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private EditText etSearch;
    private Button btnAll, btnIncome, btnExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPreferences = new UserPreferences(this);

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
        etSearch = findViewById(R.id.etSearch);
        btnAll = findViewById(R.id.btnAll);
        btnIncome = findViewById(R.id.btnIncome);
        btnExpense = findViewById(R.id.btnExpense);

        // Setup RecyclerView
        adapter = new ExpenseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this)
                .get(ExpenseViewModel.class);

        // Observe expenses
        viewModel.getAllExpenses().observe(this, expenses -> {
            allExpensesList = expenses;
            filterExpenses("ALL", "");
        });

        // Observe income
        viewModel.getTotalIncome().observe(this, income -> {
            if (income != null) {
                tvIncome.setText("₹" + String.format("%.2f", income));
            } else {
                tvIncome.setText("₹0.00");
            }
            updateBalance();
        });

        // Observe expense
        viewModel.getTotalExpense().observe(this, expense -> {
            if (expense != null) {
                tvExpense.setText("₹" +
                        String.format("%.2f", expense));
            } else {
                tvExpense.setText("₹0.00");
            }
            updateBalance();
        });

        // FAB click
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(
                    MainActivity.this, AddExpenseActivity.class
            ));
        });

        // Delete on long click
        adapter.setOnItemClickListener(
                new ExpenseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Expense expense) {}

                    @Override
                    public void onItemLongClick(Expense expense) {
                        showDeleteDialog(expense);
                    }
                });

        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) {
                filterExpenses("ALL", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter buttons
        btnAll.setOnClickListener(v ->
                filterExpenses("ALL", etSearch.getText().toString()));
        btnIncome.setOnClickListener(v ->
                filterExpenses("INCOME",
                        etSearch.getText().toString()));
        btnExpense.setOnClickListener(v ->
                filterExpenses("EXPENSE",
                        etSearch.getText().toString()));
    }

    // Filter expenses by type and search query
    private void filterExpenses(String type, String query) {
        List<Expense> filtered = new ArrayList<>();

        for (Expense expense : allExpensesList) {
            boolean matchesType = type.equals("ALL") ||
                    expense.getType().equals(type);
            boolean matchesQuery = query.isEmpty() ||
                    expense.getTitle().toLowerCase()
                            .contains(query.toLowerCase()) ||
                    expense.getCategory().toLowerCase()
                            .contains(query.toLowerCase());

            if (matchesType && matchesQuery) {
                filtered.add(expense);
            }
        }

        adapter.setExpenses(filtered);

        if (filtered.isEmpty()) {
            tvNoTransaction.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoTransaction.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
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

        if (id == R.id.action_statistics) {
            startActivity(new Intent(
                    MainActivity.this, StatisticsActivity.class
            ));
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(
                    MainActivity.this, ProfileActivity.class
            ));
            return true;
        } else if (id == R.id.action_theme) {
            toggleTheme();
            return true;
        } else if (id == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Toggle theme
    private void toggleTheme() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    // Logout dialog
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

    // Update balance
    private void updateBalance() {
        String incomeStr = tvIncome.getText()
                .toString().replace("₹", "");
        String expenseStr = tvExpense.getText()
                .toString().replace("₹", "");
        try {
            double income = Double.parseDouble(incomeStr);
            double expense = Double.parseDouble(expenseStr);
            double balance = income - expense;
            tvBalance.setText(
                    "₹" + String.format("%.2f", balance));
        } catch (NumberFormatException e) {
            tvBalance.setText("₹0.00");
        }
    }

    // Delete dialog
    private void showDeleteDialog(Expense expense) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Delete '" + expense.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) ->
                        viewModel.delete(expense))
                .setNegativeButton("Cancel", null)
                .show();
    }
}