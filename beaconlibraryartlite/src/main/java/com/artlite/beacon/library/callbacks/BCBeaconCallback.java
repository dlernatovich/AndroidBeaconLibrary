package com.artlite.beacon.library.callbacks;

import android.support.annotation.NonNull;

import com.artlite.beacon.library.beacon.Beacon;
import com.artlite.beacon.library.beacon.BeaconParser;
import com.artlite.beacon.library.beacon.BeaconTransmitter;

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
}
