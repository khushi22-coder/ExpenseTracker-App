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
import com.apexplanet.expensetracker.utils.NotificationHelper;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etDate;
    private Spinner spinnerCategory;
    private RadioGroup radioGroup;
    private RadioButton rbIncome, rbExpense;
    private Button btnSave;
    private ExpenseViewModel viewModel;
    private NotificationHelper notificationHelper;

    private String[] categories = {
            "Food", "Transport", "Shopping",
            "Bills", "Health", "Education",
            "Salary", "Business", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Transaction");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize notification helper
        notificationHelper = new NotificationHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroup = findViewById(R.id.radioGroup);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        btnSave = findViewById(R.id.btnSave);

        viewModel = new ViewModelProvider(this)
                .get(ExpenseViewModel.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCategory.setAdapter(adapter);

        etDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveExpense());
    }

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

    private void saveExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String category = spinnerCategory
                .getSelectedItem().toString();

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

        String type = rbIncome.isChecked() ? "INCOME" : "EXPENSE";
        double amount = Double.parseDouble(amountStr);

        Expense expense = new Expense(
                title, amount, category, date, type
        );
        viewModel.insert(expense);

        // Show notification
        notificationHelper.showExpenseAddedNotification(
                title, amount, type
        );

        Toast.makeText(this,
                "Transaction saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}