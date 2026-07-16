package com.apexplanet.expensetracker.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    // Insert new user
    @Insert
    void insert(User user);

    // Find user by email
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    // Check if email exists
    @Query("SELECT COUNT(*) FROM user_table WHERE email = :email")
    int checkEmailExists(String email);

    // Get user by email and password
    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);
}