package com.artlite.beacon.library.callbacks;

import android.bluetooth.le.AdvertiseSettings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Interface which provide the beacon listening
 *
 * @see com.artlite.beacon.library.managers.BCBeaconManager
 */
public interface BCBeaconCallback {

    /**
     * {@link String} value of the beacon identifier
     *
     * @return {@link String} value of the beacon identifier
     */
    @NonNull
    String getBeaconCallbackIdentifier();

    /**
     * Method which provide the action when the beacon start is failure
     *
     * @param beacon      instance of the {@link Beacon}
     * @param parser      instance of the {@link BeaconParser}
     * @param transmitter instance of the {@link BeaconTransmitter}
     * @param errorCode   {@link Integer} value of the error code
     */
    void onBeaconStartFailure(@NonNull Beacon beacon,
                              @NonNull BeaconParser parser,
                              @NonNull BeaconTransmitter transmitter,
                              int errorCode);

    /**
     * Method which provide the action when the beacon start is failure
     *
     * @param beacon      instance of the {@link Beacon}
     * @param parser      instance of the {@link BeaconParser}
     * @param transmitter instance of the {@link BeaconTransmitter}
     * @param settings    instance of the {@link AdvertiseSettings}
     */
    void onBeaconStartSuccess(@NonNull Beacon beacon,
                              @NonNull BeaconParser parser,
                              @NonNull BeaconTransmitter transmitter,
                              @NonNull AdvertiseSettings settings);

    /**
     * Method which provide the action when the beacon was stopped
     *
     * @param beacon      instance of the {@link Beacon}
     * @param parser      instance of the {@link BeaconParser}
     * @param transmitter instance of the {@link BeaconTransmitter}
     */
    void onBeaconStopped(@NonNull Beacon beacon,
                         @NonNull BeaconParser parser,
                         @NonNull BeaconTransmitter transmitter);

    /**
     * Method which provide the action when the beacon enter region
     *
     * @param region instance of the {@link Region}
     */
    void onBeaconEnterRegion(@NonNull Region region);

    /**
     * Method which provide the action when the beacon exit region
     *
     * @param region instance of the {@link Region}
     */
    void onBeaconExitRegion(@NonNull Region region);

    /**
     * Method which provide the action when the beacon exit region
     *
     * @param region instance of the {@link Region}
     * @param state  {@link Integer} value of the state, could be MonitorNotifier.INSIDE
     *               or MonitorNotifier.OUTSIDE
     */
    void onBeaconDetermineRegion(@NonNull Region region,
                                 int state);

    /**
     * Method which provide the action when the beacons found in region
     *
     * @param region  instance of the {@link Region}
     * @param beacons {@link java.util.Collections} of the {@link Beacon}
     */
    void onBeaconInsideRegion(@NonNull Region region,
                              @NonNull Collection<Beacon> beacons);
}
