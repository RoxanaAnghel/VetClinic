package com.example.bistos.myvet;

import android.app.Application;

import io.realm.RealmConfiguration;

/**
 * Created by Paul.Iovan on 12/8/2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
