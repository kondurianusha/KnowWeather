package com.example.tusharacharya.knowweather.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class KWUtils {

    public static String getAndroidID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
