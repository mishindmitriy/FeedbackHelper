package com.github.mishindmitriy.feedbackhelper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.FeedbackDialogConfig;
import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.PositiveRatingDialogConfig;
import com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs.RateDialogConfig;

import static com.github.mishindmitriy.feedbackhelper.FeedbackDataHelper.get;

/**
 * Created by mishindmitriy on 21.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class FeedbackAlertHelper {
    private final int positiveRating;
    private final FeedbackDialogConfig feedbackDialogConfig;
    private final RateDialogConfig rateDialogConfig;
    private final PositiveRatingDialogConfig positiveRatingDialogConfig;
    private final boolean showFeedbackIfRatingNotPositive;
    private final RatingFeedbackListener feedbackRatingListener;
    private final FeedbackListener feedbackListener;
    private final FeedbackCancelListener feedbackCancelListener;
    private final RatingListener ratingListener;
    private final FeedbackDataHelper dataHelper;
    private final Context context;
    private AlertDialog currentAlertDialog;

    private FeedbackAlertHelper(Builder builder) {
        context = builder.context;
        dataHelper = get();
        if (builder.positiveRating < 0 || builder.positiveRating > 5) {
            throw new IllegalArgumentException("rating must be not less than 0 and not more than 5");
        } else positiveRating = builder.positiveRating;

        feedbackDialogConfig = builder.feedbackDialogConfig;
        rateDialogConfig = builder.rateDialogConfig;
        positiveRatingDialogConfig = builder.positiveRatingDialogConfig;
        showFeedbackIfRatingNotPositive = builder.showFeedbackIfRatingNotPositive;
        feedbackRatingListener = builder.feedbackRatingListener;
        feedbackListener = builder.feedbackListener;
        feedbackCancelListener = builder.feedbackCancelListener;
        ratingListener = builder.ratingListener;
    }

    public void showRatingAlert() {
        if (currentDialogShowed()) return;
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_rate, null);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(rateDialogConfig.getTitle(context))
                .setView(view)
                .setCancelable(rateDialogConfig.isCancelable());

        if (rateDialogConfig.isShowMessage()) {
            alertDialogBuilder.setMessage(rateDialogConfig.getMessage(context));
        }

        if (rateDialogConfig.isShowNeverButton()) {
            alertDialogBuilder.setNegativeButton(R.string.never, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dataHelper.setNeverShowRateAlert();
                }
            });
        }

        if (rateDialogConfig.isShowLaterButton()) {
            alertDialogBuilder.setNeutralButton(R.string.later, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dataHelper.resetAllData();
                }
            });
        }

        currentAlertDialog = alertDialogBuilder.create();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                currentAlertDialog.dismiss();
                dataHelper.saveRating(Math.round(rating));
                if (ratingListener != null) ratingListener.onRatingSelected(rating);
                if (showFeedbackIfRatingNotPositive) {
                    checkRatingPositive(rating);
                }
            }
        });
        currentAlertDialog.show();
    }

    private void openMarket() {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            String url = "https://play.google.com/store/apps/details?id="
                    + context.getPackageName();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            try {
                context.startActivity(i);
            } catch (ActivityNotFoundException ignore) {
            }
        }
    }

    private void checkRatingPositive(final float rating) {
        if (rating > positiveRating) {
            showAskCanWriteReviewAlert();
        } else {
            showFeedbackAlert();
        }
    }

    public void showFeedbackAlert() {
        if (currentDialogShowed()) return;
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.dialog_feedback, null);
        final EditText editText = (EditText) viewGroup.findViewById(R.id.edittext);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(feedbackDialogConfig.getTitle(context));
        if (feedbackDialogConfig.isShowMessage()) {
            alertDialogBuilder.setMessage(feedbackDialogConfig.getMessage(context));
        }

        alertDialogBuilder.setView(viewGroup)
                .setCancelable(feedbackDialogConfig.isCancelable())
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().length() == 0) return;
                        dataHelper.setFeedback(editText.getText().toString());
                        if (feedbackListener != null) {
                            feedbackListener.onFeedbackSubmit(editText.getText().toString());
                        }
                        if (feedbackRatingListener != null) {
                            feedbackRatingListener.onRatingFeedback(
                                    dataHelper.getRatingValue(),
                                    editText.getText().toString()
                            );
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (feedbackCancelListener != null) {
                            feedbackCancelListener.onFeedbackCanceled(dataHelper.getRatingValue());
                        }
                    }
                });
        currentAlertDialog = alertDialogBuilder.show();
    }

    private void showAskCanWriteReviewAlert() {
        if (currentDialogShowed()) return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(positiveRatingDialogConfig.getTitle(context));
        if (positiveRatingDialogConfig.isShowMessage()) {
            builder.setMessage(positiveRatingDialogConfig.getMessage(context));
        }
        currentAlertDialog = builder.setCancelable(positiveRatingDialogConfig.isCancelable())
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openMarket();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private boolean currentDialogShowed() {
        return currentAlertDialog != null && currentAlertDialog.isShowing();
    }

    /**
     * Created by dmitriy on 23.01.17.
     */

    public static interface RatingListener {
        void onRatingSelected(float rating);
    }

    /**
     * Created by dmitriy on 23.01.17.
     */

    public static interface RatingFeedbackListener {
        void onRatingFeedback(float rating, String feedback);
    }

    /**
     * Created by dmitriy on 23.01.17.
     */

    public static interface FeedbackListener {
        void onFeedbackSubmit(String feedback);
    }

    /**
     * Created by mishindmitriy on 24.01.2017.
     */

    public static interface FeedbackCancelListener {
        void onFeedbackCanceled(float rating);
    }

    public static class Builder {
        private Context context;
        private int positiveRating = 4;
        private FeedbackDialogConfig feedbackDialogConfig = FeedbackDialogConfig.getDefault();
        private RateDialogConfig rateDialogConfig = RateDialogConfig.getDefault();
        private PositiveRatingDialogConfig positiveRatingDialogConfig = PositiveRatingDialogConfig
                .getDefault();
        private boolean showFeedbackIfRatingNotPositive = true;
        private RatingFeedbackListener feedbackRatingListener = null;
        private FeedbackListener feedbackListener = null;
        private FeedbackCancelListener feedbackCancelListener = null;
        private RatingListener ratingListener = null;

        public FeedbackAlertHelper build() {
            return new FeedbackAlertHelper(this);
        }

        public Builder from(@NonNull Activity activity) {
            this.context = activity;
            return this;
        }

        public Builder setFeedbackCancelListener(FeedbackCancelListener feedbackCancelListener) {
            this.feedbackCancelListener = feedbackCancelListener;
            return this;
        }


        public Builder setFeedbackDialogConfig(FeedbackDialogConfig feedbackDialogConfig) {
            this.feedbackDialogConfig = feedbackDialogConfig;
            return this;
        }


        public Builder setFeedbackListener(FeedbackListener feedbackListener) {
            this.feedbackListener = feedbackListener;
            return this;
        }


        public Builder setFeedbackRatingListener(RatingFeedbackListener feedbackRatingListener) {
            this.feedbackRatingListener = feedbackRatingListener;
            return this;
        }

        public Builder setPositiveRating(int positiveRating) {
            this.positiveRating = positiveRating;
            return this;
        }

        public Builder setPositiveRatingDialogConfig(PositiveRatingDialogConfig positiveRatingDialogConfig) {
            this.positiveRatingDialogConfig = positiveRatingDialogConfig;
            return this;
        }

        public Builder setRateDialogConfig(RateDialogConfig rateDialogConfig) {
            this.rateDialogConfig = rateDialogConfig;
            return this;
        }

        public Builder setRatingListener(RatingListener ratingListener) {
            this.ratingListener = ratingListener;
            return this;
        }

        public Builder setShowFeedbackIfRatingNotPositive(boolean showFeedbackIfRatingNotPositive) {
            this.showFeedbackIfRatingNotPositive = showFeedbackIfRatingNotPositive;
            return this;
        }
    }
}
