package com.apexplanet.expensetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_ONBOARDING_SEEN = "onboardingSeen";
    private static final String KEY_BUDGET = "budget";

    private SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE
        );
    }

    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
                .apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(
                KEY_IS_LOGGED_IN, false
        );
    }

    public void setUserName(String name) {
        sharedPreferences.edit()
                .putString(KEY_USER_NAME, name)
                .apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "User");
    }

    public void setUserEmail(String email) {
        sharedPreferences.edit()
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public void setOnboardingSeen(boolean seen) {
        sharedPreferences.edit()
                .putBoolean(KEY_ONBOARDING_SEEN, seen)
                .apply();
    }

    public boolean isOnboardingSeen() {
        return sharedPreferences.getBoolean(
                KEY_ONBOARDING_SEEN, false
        );
    }

    // Budget methods
    public void setBudget(double budget) {
        sharedPreferences.edit()
                .putFloat(KEY_BUDGET, (float) budget)
                .apply();
    }

    public double getBudget() {
        return sharedPreferences.getFloat(KEY_BUDGET, 0f);
    }

    public void logout() {
        boolean onboardingSeen = isOnboardingSeen();
        sharedPreferences.edit().clear().apply();
        setOnboardingSeen(onboardingSeen);
    }
}