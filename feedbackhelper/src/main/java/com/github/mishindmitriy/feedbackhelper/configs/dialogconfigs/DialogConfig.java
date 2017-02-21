package com.github.mishindmitriy.feedbackhelper.configs.dialogconfigs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;

/**
 * Created by mishindmitriy on 23.02.2017.
 * mishin.dmitriy@gmail.com
 */

public abstract class DialogConfig<T extends DialogConfig> {
    private boolean cancelable = false;
    private String title;
    private String message;
    private int titleRes;
    private int messageRes;

    public T setMessage(@StringRes int messageRes) {
        this.messageRes = messageRes;
        message = null;
        return (T) this;
    }

    public T setTitle(@StringRes int titleRes) {
        this.titleRes = titleRes;
        title = null;
        return (T) this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public T setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return (T) this;
    }

    public String getMessage(Context context) {
        return messageRes > 0 ? context.getResources().getString(messageRes) : message;
    }

    public T setMessage(String message) {
        this.message = message;
        messageRes = 0;
        return (T) this;
    }

    public boolean isShowMessage() {
        return !TextUtils.isEmpty(message) || messageRes > 0;
    }

    public String getTitle(Context context) {
        return titleRes > 0 ? context.getResources().getString(titleRes) : title;
    }

    public T setTitle(String title) {
        this.title = title;
        titleRes = 0;
        return (T) this;
    }
}
