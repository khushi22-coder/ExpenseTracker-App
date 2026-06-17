package com.apexplanet.expensetracker.presentation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.data.Expense;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etDate;
    private Spinner spinnerCategory;
    private RadioGroup radioGroup;
    private RadioButton rbIncome, rbExpense;
    private Button btnSave;
    private ExpenseViewModel viewModel;

    // Categories list
    private String[] categories = {
            "Food", "Transport", "Shopping",
            "Bills", "Health", "Education",
            "Salary", "Business", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Transaction");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Connect XML views to Java
        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroup = findViewById(R.id.radioGroup);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        btnSave = findViewById(R.id.btnSave);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Setup Category Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCategory.setAdapter(adapter);

        // Date picker when clicking date field
        etDate.setOnClickListener(v -> showDatePicker());

        // Save button click
        btnSave.setOnClickListener(v -> saveExpense());
    }

    // Show Date Picker Dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    String date = d + "/" + (m + 1) + "/" + y;
                    etDate.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Save Expense to Database
    private void saveExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        // Validation - check empty fields
        if (title.isEmpty()) {
            etTitle.setError("Please enter title");
            return;
        }
        if (amountStr.isEmpty()) {
            etAmount.setError("Please enter amount");
            return;
        }
        if (date.isEmpty()) {
            etDate.setError("Please select date");
            return;
        }

        // Get type (Income or Expense)
        String type = rbIncome.isChecked() ? "INCOME" : "EXPENSE";

        double amount = Double.parseDouble(amountStr);

        // Create new Expense object
        Expense expense = new Expense(title, amount, category, date, type);

        // Save to database
        viewModel.insert(expense);

        Toast.makeText(this, "Transaction saved!", Toast.LENGTH_SHORT).show();

        // Go back to main screen
        finish();
    }

    // Back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}