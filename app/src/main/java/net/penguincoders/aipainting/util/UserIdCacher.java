package net.penguincoders.aipainting.util;

import android.content.Context;
import android.content.SharedPreferences;


public class UserIdCacher {
    private static final String PREFERENCES_NAME = "user_preferences";
    private static final String KEY_USER_ID = "userId";

    private SharedPreferences sharedPreferences;

    public UserIdCacher(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public boolean isUserLoggedIn() {
        String userId = getUserId();
        return !userId.isEmpty();
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}

