package com.apexplanet.expensetracker.domain;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.apexplanet.expensetracker.data.ApiExpense;
import com.apexplanet.expensetracker.data.ApiService;
import com.apexplanet.expensetracker.data.Expense;
import com.apexplanet.expensetracker.data.ExpenseDao;
import com.apexplanet.expensetracker.data.ExpenseDatabase;
import com.apexplanet.expensetracker.data.RetrofitClient;
import com.apexplanet.expensetracker.utils.NetworkUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {

    private ExpenseDao expenseDao;
    private ApiService apiService;
    private Application application;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<Double> totalIncome;
    private LiveData<Double> totalExpense;
    private ExecutorService executorService =
            Executors.newFixedThreadPool(2);

    public ExpenseRepository(Application application) {
        this.application = application;
        ExpenseDatabase database =
                ExpenseDatabase.getInstance(application);
        expenseDao = database.expenseDao();
        apiService = RetrofitClient.getInstance().getApiService();
        allExpenses = expenseDao.getAllExpenses();
        totalIncome = expenseDao.getTotalIncome();
        totalExpense = expenseDao.getTotalExpense();
    }

    // Insert expense locally and to API
    public void insert(Expense expense) {
        // Save locally first
        executorService.execute(() -> expenseDao.insert(expense));

        // Save to API if internet available
        if (NetworkUtils.isInternetAvailable(application)) {
            ApiExpense apiExpense = new ApiExpense(
                    expense.getTitle(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getDate(),
                    expense.getType()
            );

            apiService.addExpense(apiExpense)
                    .enqueue(new Callback<ApiExpense>() {
                        @Override
                        public void onResponse(Call<ApiExpense> call,
                                               Response<ApiExpense> response) {
                            if (response.isSuccessful()) {
                                Log.d("API", "Expense saved to API!");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiExpense> call,
                                              Throwable t) {
                            Log.e("API", "Failed: " + t.getMessage());
                        }
                    });
        }
    }

    // Update expense
    public void update(Expense expense) {
        executorService.execute(() -> expenseDao.update(expense));
    }

    // Delete expense
    public void delete(Expense expense) {
        executorService.execute(() -> expenseDao.delete(expense));
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