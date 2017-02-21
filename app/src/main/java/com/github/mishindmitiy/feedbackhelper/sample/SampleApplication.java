package com.github.mishindmitiy.feedbackhelper.sample;

import android.app.Application;
import android.widget.Toast;

import com.github.mishindmitriy.feedbackhelper.FeedbackAlertHelper;
import com.github.mishindmitriy.feedbackhelper.FeedbackHelper;
import com.github.mishindmitriy.feedbackhelper.configs.RateAppConfig;

/**
 * Created by mishindmitriy on 19.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FeedbackHelper.startInit(this)
                .setDebug(false)
                .addConfig(
                        new RateAppConfig(1, 2).setListener(new FeedbackAlertHelper.RatingFeedbackListener() {
                            @Override
                            public void onRatingFeedback(float rating, String feedback) {
                                Toast.makeText(SampleApplication.this, feedback, Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                )
                .addConfig(new ShareAppAfterPositiveRateConfig(2))
                .init();
    }
}
