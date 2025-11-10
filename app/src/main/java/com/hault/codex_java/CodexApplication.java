package com.hault.codex_java;

import android.app.Application;
import com.hault.codex_java.util.ThemeUtils;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class CodexApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeUtils.scheduleHourlyThemeCheck(this);
        ThemeUtils.shouldRecreateActivity(this);
    }
}