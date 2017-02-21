package com.github.mishindmitiy.feedbackhelper.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.github.mishindmitriy.feedbackhelper.FeedbackDataHelper;
import com.github.mishindmitriy.feedbackhelper.configs.BaseConfig;

/**
 * Created by mishindmitriy on 21.02.2017.
 */

public class ShareAppAfterPositiveRateConfig extends BaseConfig {
    private static final String KEY_SHARE_ALERT_WAS_SHOWED = "share_dialog_was_showed";
    private static final String KEY_LAUNCH_COUNT_AFTER_RATE = "launch_times_after_rate";
    private final long appStartAfterRatingSet;

    public ShareAppAfterPositiveRateConfig(long appStartAfterRatingSet) {
        this.appStartAfterRatingSet = appStartAfterRatingSet;
    }

    private void setShareDialogShowed() {
        getDataHelper().getSharedPreferences().edit()
                .putBoolean(KEY_SHARE_ALERT_WAS_SHOWED, true)
                .remove(KEY_LAUNCH_COUNT_AFTER_RATE)
                .apply();
    }

    private boolean shareDialogIsShowed() {
        return getDataHelper().getSharedPreferences().getBoolean(KEY_SHARE_ALERT_WAS_SHOWED, false);
    }

    @Override
    public void showAlert(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.share_title)
                .setPositiveButton(
                        com.github.mishindmitriy.feedbackhelper.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setShareDialogShowed();
                                Toast.makeText(activity, "You can share app here", Toast.LENGTH_LONG)
                                        .show();
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton(com.github.mishindmitriy.feedbackhelper.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setShareDialogShowed();
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }

    @Override
    public void onAppResumed() {
        if (FeedbackDataHelper.get().ratingNotSet() || shareDialogIsShowed()) return;
        FeedbackDataHelper.get().getSharedPreferences().edit()
                .putInt(
                        KEY_LAUNCH_COUNT_AFTER_RATE,
                        FeedbackDataHelper.get().getSharedPreferences()
                                .getInt(KEY_LAUNCH_COUNT_AFTER_RATE, 0) + 1
                )
                .apply();
    }

    private int getAppStartCountAfterRatingSet() {
        return getDataHelper().getSharedPreferences().getInt(KEY_LAUNCH_COUNT_AFTER_RATE, -1);
    }

    @Override
    public boolean shouldShowAlert() {
        return !shareDialogIsShowed() && getAppStartCountAfterRatingSet() >= appStartAfterRatingSet;
    }
}
