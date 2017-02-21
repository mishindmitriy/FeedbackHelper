package com.github.mishindmitiy.feedbackhelper.sample;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.mishindmitiy.feedbackhelper.sample.databinding.ActivityMainBinding;
import com.github.mishindmitriy.feedbackhelper.FeedbackAlertHelper;
import com.github.mishindmitriy.feedbackhelper.FeedbackDataHelper;
import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.RateDialogConfig;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ActivityMainBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        FeedbackDataHelper.get().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        updateText();
    }

    private void updateText() {
        binding.launchCount.setText(
                String.format(
                        getString(R.string.data_template),
                        FeedbackDataHelper.get().getAppStartCount(),
                        FeedbackDataHelper.get().getRatingValue()
                )
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingAlert();
            }
        });
        binding.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedbackAlert();
            }
        });
        binding.rateWithFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingWithFeedbackAlert();
            }
        });
        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackDataHelper.get().resetAllData();
                updateText();
            }
        });
    }

    private void showRatingAlert() {
        new FeedbackAlertHelper.Builder()
                .from(this)
                .setRateDialogConfig(
                        RateDialogConfig.getDefault()
                                .setShowLaterButton(false)
                                .setShowNeverButton(false)
                )
                .setShowFeedbackIfRatingNotPositive(false)
                .setRatingListener(new FeedbackAlertHelper.RatingListener() {
                    @Override
                    public void onRatingSelected(float rating) {
                        Toast.makeText(
                                MainActivity.this,
                                String.format(
                                        getString(R.string.rating_set_template),
                                        rating
                                ),
                                Toast.LENGTH_LONG
                        )
                                .show();
                    }
                })
                .build()
                .showRatingAlert();
    }

    private void showFeedbackAlert() {
        new FeedbackAlertHelper.Builder()
                .from(this)
                .setFeedbackListener(new FeedbackAlertHelper.FeedbackListener() {
                    @Override
                    public void onFeedbackSubmit(String feedback) {
                        Toast.makeText(
                                MainActivity.this,
                                String.format(getString(R.string.feedback_set_template), feedback),
                                Toast.LENGTH_LONG
                        )
                                .show();
                    }
                })
                .build()
                .showFeedbackAlert();
    }

    private void showRatingWithFeedbackAlert() {
        new FeedbackAlertHelper.Builder()
                .from(this)
                .setRateDialogConfig(
                        RateDialogConfig.getDefault()
                                .setShowLaterButton(false)
                                .setShowNeverButton(false)
                )
                .setRatingListener(new FeedbackAlertHelper.RatingListener() {
                    @Override
                    public void onRatingSelected(float rating) {
                        updateText();
                    }
                })
                .setFeedbackRatingListener(new FeedbackAlertHelper.RatingFeedbackListener() {
                    @Override
                    public void onRatingFeedback(float rating, String feedback) {
                        Toast.makeText(
                                MainActivity.this,
                                String.format(
                                        getString(R.string.rating_with_feedback_set_template),
                                        rating,
                                        feedback
                                ),
                                Toast.LENGTH_LONG
                        )
                                .show();
                    }
                })
                .build()
                .showRatingAlert();
    }


    @Override
    protected void onPause() {
        FeedbackDataHelper.get().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateText();
    }
}
