package com.github.mishindmitriy.feedbackhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by mishindmitriy on 21.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class FeedbackDataHelper {
    public static final long DAY_MILLIS = 1000 * 60 * 60 * 24;
    public static final String SHARED_PREFERENCES_NAME = "FeedbackPreferences";
    public static final String KEY_INSTALL_TIMESTAMP = "app_install_timestamp";
    public static final String KEY_RATING = "rating_value";
    public static final String KEY_LAUNCH_COUNT = "launch_count";
    public static final String KEY_RATING_SET_TIMESTAMP = "rating_set_timestamp";
    public static final String KEY_NEVER_SHOW_RATING_ALERT = "never_show_rating_alert";
    private static final String KEY_FEEDBACK = "feedback";
    private static FeedbackDataHelper instance;
    private final SharedPreferences prefs;

    private FeedbackDataHelper(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    static void init(@NonNull Context context) {
        instance = new FeedbackDataHelper(context.getSharedPreferences(
                SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE)
        );
    }

    public static FeedbackDataHelper get() {
        return instance;
    }

    public void setNeverShowRateAlert() {
        prefs.edit().putBoolean(KEY_NEVER_SHOW_RATING_ALERT, true).apply();
    }

    public boolean isNeverShowRateAlert() {
        return prefs.getBoolean(KEY_NEVER_SHOW_RATING_ALERT, false);
    }

    public int getRatingValue() {
        return prefs.getInt(KEY_RATING, -1);
    }

    public boolean containsInstallTimestamp() {
        return prefs.contains(KEY_INSTALL_TIMESTAMP);
    }

    public SharedPreferences getSharedPreferences() {
        return prefs;
    }

    public int getAppStartCount() {
        return prefs.getInt(KEY_LAUNCH_COUNT, 0);
    }

    public void setAppStartCount(int count) {
        prefs.edit().putInt(KEY_LAUNCH_COUNT, count).apply();
    }

    public boolean ratingNotSet() {
        return prefs.getInt(KEY_RATING, -1) == -1;
    }

    public boolean ratingWasSet() {
        return prefs.getInt(KEY_RATING, -1) > -1;
    }

    public long getInstallTimestamp() {
        return prefs.getLong(KEY_INSTALL_TIMESTAMP, 0);
    }

    public void setInstallTimestamp(long timestamp) {
        prefs.edit()
                .putLong(KEY_INSTALL_TIMESTAMP, timestamp)
                .apply();
    }

    public void resetAllData() {
        prefs.edit().clear()
                .putLong(KEY_INSTALL_TIMESTAMP, now())
                .apply();
    }

    public void saveRating(int rating) {
        prefs.edit()
                .putInt(KEY_RATING, rating)
                .putLong(KEY_RATING_SET_TIMESTAMP, FeedbackDataHelper.now())
                .remove(KEY_LAUNCH_COUNT)
                .apply();
    }

    public void removeRating() {
        prefs.edit()
                .remove(KEY_RATING)
                .remove(KEY_RATING_SET_TIMESTAMP)
                .apply();
    }

    public String getFeedback() {
        return prefs.getString(KEY_FEEDBACK, "");
    }

    public void setFeedback(String text) {
        prefs.edit().putString(KEY_FEEDBACK, text).apply();
    }
}
