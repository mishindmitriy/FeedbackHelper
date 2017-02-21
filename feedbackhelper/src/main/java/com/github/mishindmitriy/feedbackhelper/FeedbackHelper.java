package com.github.mishindmitriy.feedbackhelper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.mishindmitriy.feedbackhelper.configs.ConfigCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by mishindmitriy on 23.01.17.
 * mishin.dmitriy@gmail.com
 */

public class FeedbackHelper {
    private static FeedbackHelper instance;
    private final FeedbackDataHelper prefsHelper;

    private FeedbackHelper() {
        prefsHelper = FeedbackDataHelper.get();
        if (!prefsHelper.containsInstallTimestamp()) {
            prefsHelper.setInstallTimestamp(FeedbackDataHelper.now());
        }
    }

    public static InitBuilder startInit(Application application) {
        FeedbackDataHelper.init(application);
        return new InitBuilder(application);
    }

    private static void init(final InitBuilder builder) {
        instance = new FeedbackHelper();
        builder.application.registerActivityLifecycleCallbacks(createAppStartWatcher(
                builder.configs
        ));
        builder.application.registerActivityLifecycleCallbacks(createOnActivityResumeWatcher(
                builder.debug, builder.configs
        ));
    }

    static Application.ActivityLifecycleCallbacks createOnActivityResumeWatcher(
            final boolean debug, final List<ConfigCallback> configs
    ) {
        return new ActivityOnResumeWatcher(new ActivityOnResumeWatcher.OnActivityResumeListener() {
            @Override
            public void onActivityResumed(Activity activity) {
                for (ConfigCallback config : configs) {
                    if (debug || config.shouldShowAlert()) config.showAlert(activity);
                }
            }
        });
    }

    private static Application.ActivityLifecycleCallbacks createAppStartWatcher(final List<ConfigCallback> configs) {
        return new Application.ActivityLifecycleCallbacks() {
            private int resumeCount = 0;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (resumeCount == 0) {
                    for (ConfigCallback config : configs) {
                        config.onAppResumed();
                    }
                }
                resumeCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                resumeCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    public static FeedbackHelper get() {
        return instance;
    }

    public void incrementAppStart() {
        if (prefsHelper.ratingWasSet() || prefsHelper.isNeverShowRateAlert()) return;
        prefsHelper.setAppStartCount(prefsHelper.getAppStartCount() + 1);
    }

    public static class InitBuilder {
        private final Application application;
        private List<ConfigCallback> configs = new ArrayList<>();
        private boolean debug;

        private InitBuilder(Application application) {
            this.application = application;
        }

        public void init() {
            FeedbackHelper.init(this);
        }

        public InitBuilder addConfig(ConfigCallback config) {
            configs.add(config);
            return this;
        }

        //if debug=true, method showAlert in ConfigCallback will be called always when
        // application resumed, and method shouldShowAlert will not be called
        public InitBuilder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }
    }

    /**
     * Created by mishindmitriy on 24.01.2017.
     */

    public static class ActivityOnResumeWatcher implements Application.ActivityLifecycleCallbacks {
        private final OnActivityResumeListener onActivityResumeListener;
        private final Set<Class<? extends Activity>> excludeActivitiesClasses = new HashSet<>();

        public ActivityOnResumeWatcher(@NonNull OnActivityResumeListener onActivityResumeListener) {
            this.onActivityResumeListener = onActivityResumeListener;
        }

        public ActivityOnResumeWatcher(@NonNull OnActivityResumeListener onActivityResumeListener,
                                       Class<? extends Activity>... activities) {
            this(onActivityResumeListener);
            Collections.addAll(excludeActivitiesClasses, activities);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (excludeActivitiesClasses.contains(activity.getClass())) return;
            onActivityResumeListener.onActivityResumed(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        public interface OnActivityResumeListener {
            void onActivityResumed(Activity activity);
        }
    }
}
