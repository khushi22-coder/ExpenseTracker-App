package com.apexplanet.expensetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_ONBOARDING_SEEN = "onboardingSeen";

    private SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE
        );
    }

    // Save login state
    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
                .apply();
    }

    // Check if logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Save user name
    public void setUserName(String name) {
        sharedPreferences.edit()
                .putString(KEY_USER_NAME, name)
                .apply();
    }

    // Get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "User");
    }

    // Save user email
    public void setUserEmail(String email) {
        sharedPreferences.edit()
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    // Get user email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    // Save onboarding seen
    public void setOnboardingSeen(boolean seen) {
        sharedPreferences.edit()
                .putBoolean(KEY_ONBOARDING_SEEN, seen)
                .apply();
    }

    // Check if onboarding seen
    public boolean isOnboardingSeen() {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_SEEN, false);
    }

    // Logout
    public void logout() {
        sharedPreferences.edit().clear().apply();
    }
}