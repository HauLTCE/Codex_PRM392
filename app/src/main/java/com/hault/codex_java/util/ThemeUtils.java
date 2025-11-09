package com.hault.codex_java.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import com.hault.codex_java.MainActivity;
import com.hault.codex_java.R;
import java.util.Calendar;

public class ThemeUtils {
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String KEY_LAST_THEME = "lastTheme";

    public static void applyTheme(Context context) {
        context.setTheme(getThemeByTime());
    }

    public static int getThemeByTime() {
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

    public static void scheduleHourlyThemeCheck(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ThemeCheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long intervalMillis = 60 * 60 * 1000; // 1 hour
        long triggerAtMillis = SystemClock.elapsedRealtime() + intervalMillis;

        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerAtMillis,
                intervalMillis,
                pendingIntent
        );
    }

    public static boolean shouldRecreateActivity(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int lastTheme = prefs.getInt(KEY_LAST_THEME, -1);
        int currentTheme = getThemeByTime();

        if (lastTheme != currentTheme) {
            prefs.edit().putInt(KEY_LAST_THEME, currentTheme).apply();
            return true;
        }
        return false;
    }

    public static class ThemeCheckReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (shouldRecreateActivity(context)) {
                Intent restartIntent = new Intent(context, MainActivity.class);
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(restartIntent);
            }
        }
    }
}