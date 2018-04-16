package com.artlite.beaconlibrary;

import android.app.Application;

import com.artlite.beacon.library.managers.BCBeaconManager;
import com.artlite.bslibrary.core.BSInstance;

public final class CurrentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BSInstance.init(this);
        BCBeaconManager.init(this);
    }
}
