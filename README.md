# FeedbackHelper
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

This is a library that helps to show the dialogues on the basis of various data (such as dialogue with the rating request after 10 days after installation). The library includes a configuration to request the rating on the expiration of a specified quantity of days or count of launches application. You can also write a configuration that will work just as you need. This will help you in the promotion of applications, quickly and easily.

## Using
Initialise helper in application onCreate() method
```java
int daysAfterInstall=10;
int startAppCountAfterInstall=10;
FeedbackHelper.startInit(this)
                .addConfig(
                        new RateAppConfig(daysAfterInstall, startAppCountAfterInstall)
                        .setListener(new FeedbackAlertHelper.RatingFeedbackListener() {
                            @Override
                            public void onRatingFeedback(float rating, String feedback) {
                                //use this method for send feedback to your server or slack channel for example
                            }
                        })
                )
                .addConfig(//You can add custom config)
                .init();
```

## Default dialogs
You can use default dialogs with your configuration. For example, you can show just rate alert dialog without buttons and with custom title and/or message:
```java
new FeedbackAlertHelper.Builder()
                .from(this)
                .setRateDialogConfig(
                        RateDialogConfig.getDefault()
                                .setTitle("Custom title")
                                .setShowLaterButton(false)
                                .setShowNeverButton(false)
                )
                .setShowFeedbackIfRatingNotPositive(false)
                .setRatingListener(new FeedbackAlertHelper.RatingListener() {
                    @Override
                    public void onRatingSelected(float rating) {

                    }
                })
                .build()
                .showRatingAlert();
```

Available default dialogs:
- Rating dialog
- Feedback dialog

To get feedback or rating, you should set suitable listener, or you can get data from FeedbackDataHelper later

## Custom config (See [ShareAppAfterPositiveRateConfig](https://github.com/mishindmitriy/FeedbackHelper/blob/master/app/src/main/java/com/github/mishindmitiy/feedbackhelper/sample/ShareAppAfterPositiveRateConfig.java) or [RateAppConfig](https://github.com/mishindmitriy/FeedbackHelper/blob/master/feedbackhelper/src/main/java/com/github/mishindmitriy/feedbackhelper/configs/RateAppConfig.java) example for details)
```java
public class CustomConfig extends BaseConfig {
    @Override
    public void showAlert(final Activity activity) {
        //show suitable alert
    }

    @Override
    public void onAppResumed() {
        //do something with your data, for example increment some field
        //sharedPreferences available with getDataHelper() for saving your fields
    }

    @Override
    public boolean shouldShowAlert() {
        return false;
    }
}
```

## ProGuard
No special ProGuard rules required.

