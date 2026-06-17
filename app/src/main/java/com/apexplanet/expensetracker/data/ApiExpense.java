package com.apexplanet.expensetracker.data;

import com.google.gson.annotations.SerializedName;

public class ApiExpense {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("amount")
    private double amount;

    @SerializedName("category")
    private String category;

    @SerializedName("date")
    private String date;

    @SerializedName("type")
    private String type;

    // Constructor
    public ApiExpense(String title, double amount,
                      String category, String date, String type) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.type = type;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getType() { return type; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setType(String type) { this.type = type; }
}