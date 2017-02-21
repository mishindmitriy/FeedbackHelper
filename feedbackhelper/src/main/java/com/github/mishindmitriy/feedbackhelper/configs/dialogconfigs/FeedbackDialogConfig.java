package com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs;

import com.github.mishindmitriy.feedbackhelper.R;

/**
 * Created by mishindmitriy on 23.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class FeedbackDialogConfig extends DialogConfig<FeedbackDialogConfig> {
    public static FeedbackDialogConfig getDefault() {
        return new FeedbackDialogConfig()
                .setTitle(R.string.feedback_title)
                .setMessage(R.string.feedback_message);
    }
}
