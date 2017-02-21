package com.github.mishindmitriy.feedbackhelper.configs;

import android.app.Activity;

/**
 * Created by mishindmitriy on 21.02.2017.
 * mishin.dmitriy@gmail.com
 */

public interface ConfigCallback {
    void showAlert(Activity activity);

    void onAppResumed();

    boolean shouldShowAlert();
}
