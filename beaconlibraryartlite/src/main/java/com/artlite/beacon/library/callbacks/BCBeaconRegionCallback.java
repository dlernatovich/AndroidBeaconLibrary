package com.artlite.beacon.library.callbacks;

import android.bluetooth.le.AdvertiseSettings;
import android.support.annotation.NonNull;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Region;


/**
 * Class which provide the short cut of the {@link BCBeaconCallback}
 */
public abstract class BCBeaconRegionCallback implements BCBeaconCallback {

    /**
     * {@link String} value of the tag
     */
    private static String K_TAG = BCBeaconRegionCallback.class.getSimpleName();

    /**
     * Method which provide the action when the beacon start is failure
     *
     * @param beacon      instance of the {@link Beacon}
     * @param parser      instance of the {@link BeaconParser}
     * @param transmitter instance of the {@link BeaconTransmitter}
     * @param errorCode   {@link Integer} value of the error code
     */
    @Override
    public void onBeaconStartFailure(@NonNull Beacon beacon,
                                     @NonNull BeaconParser parser,
                                     @NonNull BeaconTransmitter transmitter,
                                     int errorCode) {
        Log.e(K_TAG, "onBeaconStartFailure");
    }

    /**
     * Method which provide the action when the beacon start is failure
     *
     * @param beacon      instance of the {@link Beacon}
     * @param parser      instance of the {@link BeaconParser}
     * @param transmitter instance of the {@link BeaconTransmitter}
     * @param settings    instance of the {@link AdvertiseSettings}
     */
    @Override
    public void onBeaconStartSuccess(@NonNull Beacon beacon,
                                     @NonNull BeaconParser parser,
                                     @NonNull BeaconTransmitter transmitter,
                                     @NonNull AdvertiseSettings settings) {
        Log.e(K_TAG, "onBeaconStartSuccess");
    }

    /**
     * Method which provide the action when the beacon was stopped
     *
     * @param beacon      instance of the {@link Beacon}
     * @param parser      instance of the {@link BeaconParser}
     * @param transmitter instance of the {@link BeaconTransmitter}
     */
    @Override
    public void onBeaconStopped(@NonNull Beacon beacon,
                                @NonNull BeaconParser parser,
                                @NonNull BeaconTransmitter transmitter) {
        Log.e(K_TAG, "onBeaconStopped");
    }

    /**
     * Method which provide the action when the beacon enter region
     *
     * @param region instance of the {@link Region}
     */
    @Override
    public void onBeaconEnterRegion(@NonNull Region region) {
        Log.e(K_TAG, "onBeaconEnterRegion");
    }

    /**
     * Method which provide the action when the beacon exit region
     *
     * @param region instance of the {@link Region}
     */
    @Override
    public void onBeaconExitRegion(@NonNull Region region) {
        Log.e(K_TAG, "onBeaconExitRegion");
    }

    /**
     * Method which provide the action when the beacon exit region
     *
     * @param region instance of the {@link Region}
     * @param state  {@link Integer} value of the state, could be MonitorNotifier.INSIDE
     */
    @Override
    public void onBeaconDetermineRegion(@NonNull Region region, int state) {
        Log.e(K_TAG, "onBeaconDetermineRegion");
    }

}
