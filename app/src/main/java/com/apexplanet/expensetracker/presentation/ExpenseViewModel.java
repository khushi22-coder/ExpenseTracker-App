package com.apexplanet.expensetracker.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.apexplanet.expensetracker.data.Expense;
import com.apexplanet.expensetracker.domain.ExpenseRepository;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<Double> totalIncome;
    private LiveData<Double> totalExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        totalIncome = repository.getTotalIncome();
        totalExpense = repository.getTotalExpense();
    }

    // Insert
    public void insert(Expense expense) {
        repository.insert(expense);
    }

    // Update
    public void update(Expense expense) {
        repository.update(expense);
    }

    // Delete
    public void delete(Expense expense) {
        repository.delete(expense);
    }

    // Get all expenses
    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    // Get total income
    public LiveData<Double> getTotalIncome() {
        return totalIncome;
    }

    // Get total expense
    public LiveData<Double> getTotalExpense() {
        return totalExpense;
    }
}