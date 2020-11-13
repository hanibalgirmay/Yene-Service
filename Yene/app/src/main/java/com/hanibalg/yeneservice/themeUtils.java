package com.hanibalg.yeneservice;

import android.app.Activity;
import android.content.Intent;

public class themeUtils {
    private static int cTheme;
    public final static int BLACK = 0;
    public final static int BLUE = 1;

    public static void changeToTheme(Activity activity, int theme) {
        cTheme = theme;
        activity.finish();
        onActivityCreateSetTheme(activity);
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (cTheme) {
            default:
            case BLACK:
                activity.setTheme(R.style.Theme_MaterialComponents_DayNight_NoActionBar);
                break;
            case BLUE:
                activity.setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar);
                break;
        }
    }
}
