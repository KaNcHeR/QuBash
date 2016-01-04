package com.agrotrading.kancher.qubash;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class DBFlowTaskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }

}
