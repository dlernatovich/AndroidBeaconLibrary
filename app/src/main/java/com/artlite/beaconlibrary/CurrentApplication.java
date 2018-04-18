package com.artlite.beaconlibrary;

import android.app.Application;
import android.support.annotation.NonNull;

import com.artlite.beacon.library.beacon.Beacon;
import com.artlite.beacon.library.beacon.BeaconManager;
import com.artlite.beacon.library.beacon.Region;
import com.artlite.beacon.library.callbacks.BCBeaconRegionCallback;
import com.artlite.beacon.library.managers.BCBeaconManager;
import com.artlite.bslibrary.core.BSInstance;
import com.artlite.bslibrary.helpers.notification.BSNotificationHelper;
import com.artlite.bslibrary.managers.BSContextManager;

import java.util.Collection;

public final class CurrentApplication extends Application {

    private BCBeaconRegionCallback regionCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        BSInstance.init(this);
        BCBeaconManager.init(this);
        BCBeaconManager.addCallback(getRegionCallback());
    }

    public BCBeaconRegionCallback getRegionCallback() {
        if (regionCallback == null) {
            regionCallback = new BCBeaconRegionCallback() {
                @NonNull
                @Override
                public String getBeaconCallbackIdentifier() {
                    return null;
                }

                @Override
                public void onBeaconInsideRegion(@NonNull Region region,
                                                 @NonNull Collection<Beacon> beacons) {
                    BSNotificationHelper.showNotification(BSContextManager.getApplicationContext(),
                            "Beacon found",
                            "You found the beacon");

                }
            };
        }
        return regionCallback;
    }
}
