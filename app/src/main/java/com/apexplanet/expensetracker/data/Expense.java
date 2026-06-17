package com.apexplanet.expensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private double amount;
    private String category;
    private String date;
    private String type; // "INCOME" or "EXPENSE"

    // Constructor
    public Expense(String title, double amount,
                   String category, String date, String type) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.type = type;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getType() { return type; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setType(String type) { this.type = type; }
}