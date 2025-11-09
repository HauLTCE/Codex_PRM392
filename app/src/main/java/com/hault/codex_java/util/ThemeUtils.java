package com.hault.codex_java.util;

import android.content.Context;

import com.hault.codex_java.R;

import java.util.Calendar;

public class ThemeUtils {

    public static void applyTheme(Context context) {
        context.setTheme(getThemeByTime());
    }

    private static int getThemeByTime() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (currentHour >= 5 && currentHour < 12) {
            return R.style.Theme_Codex_Morning;
        } else if (currentHour >= 12 && currentHour < 18) {
            return R.style.Theme_Codex_Day;
        } else if (currentHour >= 18 && currentHour < 22) {
            return R.style.Theme_Codex_Evening;
        } else {
            return R.style.Theme_Codex_Night;
        }
    }
}