package com.github.mishindmitriy.feedbackhelper.configs;

import android.app.Activity;

import com.github.mishindmitriy.feedbackhelper.FeedbackAlertHelper;
import com.github.mishindmitriy.feedbackhelper.FeedbackDataHelper;
import com.github.mishindmitriy.feedbackhelper.FeedbackHelper;
import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.FeedbackDialogConfig;
import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.PositiveRatingDialogConfig;
import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.RateDialogConfig;

/**
 * Created by mishindmitriy on 21.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class RateAppConfig extends BaseConfig {
    private final long daysFromInstall;
    private final long appStartCountFromInstall;
    private FeedbackAlertHelper.RatingFeedbackListener listener;
    private FeedbackDialogConfig feedbackDialogConfig = FeedbackDialogConfig.getDefault();
    private RateDialogConfig rateDialogConfig = RateDialogConfig.getDefault();
    private PositiveRatingDialogConfig positiveRatingDialogConfig = PositiveRatingDialogConfig
            .getDefault();

    public RateAppConfig(long daysFromInstall, long appStartCountFromInstall) {
        super();
        if (daysFromInstall <= 0) {
            throw new IllegalArgumentException("days from install must be more then 0");
        }
        this.daysFromInstall = daysFromInstall;
        if (appStartCountFromInstall <= 0) {
            throw new IllegalArgumentException("app start count from install must be more then 0");
        }
        this.appStartCountFromInstall = appStartCountFromInstall;
    }

    public RateAppConfig setListener(FeedbackAlertHelper.RatingFeedbackListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void showAlert(Activity activity) {
        new FeedbackAlertHelper.Builder()
                .from(activity)
                .setRateDialogConfig(rateDialogConfig)
                .setFeedbackDialogConfig(feedbackDialogConfig)
                .setPositiveRatingDialogConfig(positiveRatingDialogConfig)
                .setFeedbackRatingListener(listener)
                .build()
                .showRatingAlert();
    }

    public RateAppConfig setFeedbackDialogConfig(FeedbackDialogConfig feedbackDialogConfig) {
        this.feedbackDialogConfig = feedbackDialogConfig;
        return this;
    }

    public RateAppConfig setPositiveRatingDialogConfig(PositiveRatingDialogConfig positiveRatingDialogConfig) {
        this.positiveRatingDialogConfig = positiveRatingDialogConfig;
        return this;
    }

    public RateAppConfig setRateDialogConfig(RateDialogConfig rateDialogConfig) {
        this.rateDialogConfig = rateDialogConfig;
        return this;
    }

    @Override
    public void onAppResumed() {
        FeedbackHelper.get().incrementAppStart();
    }

    @Override
    public boolean shouldShowAlert() {
        return !getDataHelper().isNeverShowRateAlert() && getDataHelper().ratingNotSet()
                && (timePassedMoreThanNeed() || openedMoreTimesThanNeed());
    }

    private boolean openedMoreTimesThanNeed() {
        return getDataHelper().getAppStartCount() >= appStartCountFromInstall;
    }

    private boolean timePassedMoreThanNeed() {
        return FeedbackDataHelper.now() - FeedbackDataHelper.DAY_MILLIS * daysFromInstall
                > FeedbackDataHelper.get().getInstallTimestamp();
    }
}
