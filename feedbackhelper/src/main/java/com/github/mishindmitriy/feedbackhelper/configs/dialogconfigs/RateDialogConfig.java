package com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs;

import com.github.mishindmitriy.feedbackhelper.R;

/**
 * Created by mishindmitriy on 23.02.2017.
 * mishin.dmitriy@gmail.com
 */

public class RateDialogConfig extends DialogConfig<RateDialogConfig> {
    private boolean showNeverButton = true;
    private boolean showLaterButton = true;

    public static RateDialogConfig getDefault() {
        return new RateDialogConfig()
                .setMessage(R.string.rate_title);
    }

    public boolean isShowLaterButton() {
        return showLaterButton;
    }

    public RateDialogConfig setShowLaterButton(boolean showLaterButton) {
        this.showLaterButton = showLaterButton;
        return this;
    }

    public boolean isShowNeverButton() {
        return showNeverButton;
    }

    public RateDialogConfig setShowNeverButton(boolean showNeverButton) {
        this.showNeverButton = showNeverButton;
        return this;
    }
}
