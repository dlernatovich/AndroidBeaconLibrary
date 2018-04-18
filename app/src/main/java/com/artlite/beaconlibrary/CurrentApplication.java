package com.artlite.beaconlibrary;

import android.app.Application;
import android.support.annotation.NonNull;


import com.artlite.beacon.library.callbacks.BCBeaconRegionCallback;
import com.artlite.beacon.library.managers.BCBeaconManager;
import com.artlite.bslibrary.core.BSInstance;
import com.artlite.bslibrary.helpers.notification.BSNotificationHelper;
import com.artlite.bslibrary.managers.BSContextManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Collections;

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
                /**
                 * {@link String} value of the beacon identifier
                 *
                 * @return {@link String} value of the beacon identifier
                 */
                @NonNull
                @Override
                public String getBeaconCallbackIdentifier() {
                    return null;
                }

                /**
                 * Method which provide the action when the beacons found in region
                 *
                 * @param region  instance of the {@link Region}
                 * @param beacons {@link Collections} of the {@link Beacon}
                 */
                @Override
                public void onBeaconInsideRegion(@NonNull Region region, @NonNull Collection<Beacon> beacons) {
                    BSNotificationHelper.showNotification(BSContextManager.getApplicationContext(),
                            "You received the beacon", "New beacon was received");
                }
            };
        }
        return regionCallback;
    }
}
