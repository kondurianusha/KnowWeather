package com.example.tusharacharya.knowweather;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class KWApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

    }
}
