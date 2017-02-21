package com.github.mishindmitriy.feedbackhelper.configs;

import com.github.mishindmitriy.feedbackhelper.FeedbackDataHelper;

/**
 * Created by mishindmitriy on 21.02.2017.
 * mishin.dmitriy@gmail.com
 */

public abstract class BaseConfig implements ConfigCallback {
    private final FeedbackDataHelper dataHelper;

    protected BaseConfig() {
        dataHelper = FeedbackDataHelper.get();
    }

    protected FeedbackDataHelper getDataHelper() {
        return dataHelper;
    }
}
