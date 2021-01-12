package com.example.camp_proj1;

import android.app.Activity;
import android.content.Intent;
public class Utils
{
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_ORANGE = 1;
    public final static int THEME_PINK = 2;
    public final static int THEME_BLACK = 3;
    public final static int THEME_RED = 4;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.Theme_Camp_proj1);
                break;
            case THEME_ORANGE:
                activity.setTheme(R.style.Theme_Camp_proj3);
                break;
            case THEME_PINK:
                activity.setTheme(R.style.Theme_Camp_proj4);
                break;

            case THEME_BLACK:
                activity.setTheme(R.style.Theme_Camp_proj5);
                break;

            case THEME_RED:
                activity.setTheme(R.style.Theme_Camp_proj6);
                break;
        }
    }
}
