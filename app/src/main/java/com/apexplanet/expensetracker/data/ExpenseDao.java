package com.apexplanet.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    // Add new expense
    @Insert
    void insert(Expense expense);

    // Update existing expense
    @Update
    void update(Expense expense);

    // Delete expense
    @Delete
    void delete(Expense expense);

    // Get all expenses (newest first)
    @Query("SELECT * FROM expense_table ORDER BY id DESC")
    LiveData<List<Expense>> getAllExpenses();

    // Get total income
    @Query("SELECT SUM(amount) FROM expense_table WHERE type = 'INCOME'")
    LiveData<Double> getTotalIncome();

    // Get total expense
    @Query("SELECT SUM(amount) FROM expense_table WHERE type = 'EXPENSE'")
    LiveData<Double> getTotalExpense();
}