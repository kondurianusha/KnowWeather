package com.tramsun.knowweather;

import android.app.Application;

import timber.log.Timber;

public class KWApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

    }
}
