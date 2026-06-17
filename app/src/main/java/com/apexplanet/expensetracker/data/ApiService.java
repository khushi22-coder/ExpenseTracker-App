package com.apexplanet.expensetracker.data;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Get all expenses
    @GET("expenses")
    Call<List<ApiExpense>> getExpenses();

    // Add new expense
    @POST("expenses")
    Call<ApiExpense> addExpense(@Body ApiExpense expense);

    // Update expense
    @PUT("expenses/{id}")
    Call<ApiExpense> updateExpense(
            @Path("id") String id,
            @Body ApiExpense expense
    );

    // Delete expense
    @DELETE("expenses/{id}")
    Call<ApiExpense> deleteExpense(@Path("id") String id);
}