package com.apexplanet.expensetracker.presentation;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.data.Expense;
import com.apexplanet.expensetracker.utils.UserPreferences;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private TextView tvStatIncome, tvStatExpense, tvBudgetStatus;
    private EditText etBudget;
    private Button btnSetBudget;
    private ProgressBar progressBudget;
    private ExpenseViewModel viewModel;
    private UserPreferences userPreferences;

    private double totalIncome = 0;
    private double totalExpense = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Statistics 📊");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userPreferences = new UserPreferences(this);

        // Connect views
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        tvStatIncome = findViewById(R.id.tvStatIncome);
        tvStatExpense = findViewById(R.id.tvStatExpense);
        tvBudgetStatus = findViewById(R.id.tvBudgetStatus);
        etBudget = findViewById(R.id.etBudget);
        btnSetBudget = findViewById(R.id.btnSetBudget);
        progressBudget = findViewById(R.id.progressBudget);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this)
                .get(ExpenseViewModel.class);

        // Observe expenses
        viewModel.getAllExpenses().observe(this, expenses -> {
            updateCharts(expenses);
        });

        // Observe income
        viewModel.getTotalIncome().observe(this, income -> {
            totalIncome = income != null ? income : 0;
            tvStatIncome.setText("₹" +
                    String.format("%.2f", totalIncome));
            updateBudgetProgress();
        });

        // Observe expense
        viewModel.getTotalExpense().observe(this, expense -> {
            totalExpense = expense != null ? expense : 0;
            tvStatExpense.setText("₹" +
                    String.format("%.2f", totalExpense));
            updateBudgetProgress();
        });

        // Set budget button
        btnSetBudget.setOnClickListener(v -> setBudget());

        // Load saved budget
        double savedBudget = userPreferences.getBudget();
        if (savedBudget > 0) {
            etBudget.setText(String.valueOf(savedBudget));
        }
    }

    // Update pie and bar charts
    private void updateCharts(List<Expense> expenses) {
        // Category wise expense map
        Map<String, Float> categoryMap = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getType().equals("EXPENSE")) {
                String category = expense.getCategory();
                float amount = (float) expense.getAmount();
                categoryMap.put(category,
                        categoryMap.getOrDefault(category, 0f)
                                + amount);
            }
        }

        // Setup Pie Chart
        setupPieChart(categoryMap);

        // Setup Bar Chart
        setupBarChart();
    }

    // Setup Pie Chart
    private void setupPieChart(Map<String, Float> categoryMap) {
        List<PieEntry> entries = new ArrayList<>();

        if (categoryMap.isEmpty()) {
            entries.add(new PieEntry(1f, "No Data"));
        } else {
            for (Map.Entry<String, Float> entry :
                    categoryMap.entrySet()) {
                entries.add(new PieEntry(
                        entry.getValue(), entry.getKey()
                ));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(14f);
        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);

        pieChart.invalidate();
    }

    // Setup Bar Chart
    private void setupBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) totalIncome));
        entries.add(new BarEntry(1f, (float) totalExpense));

        BarDataSet dataSet = new BarDataSet(
                entries, "Income vs Expense"
        );
        dataSet.setColors(
                Color.parseColor("#4CAF50"),
                Color.parseColor("#F44336")
        );
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    // Set budget limit
    private void setBudget() {
        String budgetStr = etBudget.getText().toString().trim();
        if (budgetStr.isEmpty()) {
            etBudget.setError("Please enter budget amount");
            return;
        }

        double budget = Double.parseDouble(budgetStr);
        userPreferences.setBudget(budget);
        Toast.makeText(this,
                "Budget set to ₹" +
                        String.format("%.2f", budget),
                Toast.LENGTH_SHORT).show();
        updateBudgetProgress();
    }

    // Update budget progress bar
    private void updateBudgetProgress() {
        double budget = userPreferences.getBudget();
        if (budget > 0) {
            int progress = (int) ((totalExpense / budget) * 100);
            progressBudget.setProgress(
                    Math.min(progress, 100)
            );

            if (totalExpense > budget) {
                tvBudgetStatus.setText(
                        "⚠️ Over budget by ₹" +
                                String.format("%.2f",
                                        totalExpense - budget)
                );
                tvBudgetStatus.setTextColor(
                        getResources().getColor(R.color.expense_red)
                );
            } else {
                tvBudgetStatus.setText(
                        "✅ ₹" +
                                String.format("%.2f",
                                        budget - totalExpense) +
                                " remaining"
                );
                tvBudgetStatus.setTextColor(
                        getResources().getColor(R.color.income_green)
                );
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}