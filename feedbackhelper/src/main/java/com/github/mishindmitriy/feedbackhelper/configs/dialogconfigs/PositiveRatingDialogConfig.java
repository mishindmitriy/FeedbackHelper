package com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs;

import com.github.mishindmitriy.feedbackhelper.R;

/**
 * Created by mishindmitriy on 23.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class PositiveRatingDialogConfig extends DialogConfig<PositiveRatingDialogConfig> {
    public static PositiveRatingDialogConfig getDefault() {
        return new PositiveRatingDialogConfig()
                .setTitle(R.string.review_title)
                .setMessage(R.string.review_message);
    }
}
