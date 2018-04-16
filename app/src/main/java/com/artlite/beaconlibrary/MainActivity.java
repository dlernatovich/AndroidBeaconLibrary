package com.artlite.beaconlibrary;

import android.annotation.SuppressLint;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.artlite.beacon.library.beacon.Beacon;
import com.artlite.beacon.library.beacon.BeaconParser;
import com.artlite.beacon.library.beacon.BeaconTransmitter;
import com.artlite.beacon.library.beacon.Region;
import com.artlite.beacon.library.callbacks.BCBeaconCallback;
import com.artlite.beacon.library.managers.BCBeaconManager;
import com.artlite.bslibrary.annotations.FindViewBy;
import com.artlite.bslibrary.helpers.log.BSLogHelper;
import com.artlite.bslibrary.ui.activity.BSActivity;
import com.artlite.bslibrary.ui.fonted.BSEditText;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class MainActivity extends BSActivity {

    /**
     * Instance of the {@link BSEditText}
     */
    @FindViewBy(id = R.id.edit_minor)
    private BSEditText editMinor;

    /**
     * Instance of the {@link BSEditText}
     */
    @FindViewBy(id = R.id.edit_major)
    private BSEditText editMajor;

    /**
     * Instance of the {@link BeaconTransmitter}
     */
    private BeaconTransmitter transmitter;

    /**
     * Method which provide the getting of the layout ID for the current Activity
     *
     * @return layout ID for the current Activity
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * Method which provide the action when Activity is created
     *
     * @param bundle
     */
    @Override
    protected void onCreateActivity(@Nullable Bundle bundle) {

    }

    /**
     * Method which provide the action when Activity is created (post creation)
     * Use it if you create any callback inside the activity like
     * <b>final |CallbackType| callback = new |CallbackType|</b>
     *
     * @param bundle
     */
    @Override
    protected void onActivityPostCreation(@Nullable Bundle bundle) {
        setOnClickListeners(R.id.button_start, R.id.button_stop);
    }

    /**
     * Overriden method for the OnClickListener
     *
     * @param v current view
     */
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start: {
                startBeacon();
                break;
            }
            case R.id.button_stop: {
                stopBeacon();
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * Method which provide the beacon starting
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void startBeacon() {
        BCBeaconManager.startBeacon(this.beaconCallback);
    }

    /**
     * Method which provide the beacon stop
     */
    protected void stopBeacon() {
        BCBeaconManager.stopBeacon();
    }

    /**
     * Instance of the {@link BCBeaconCallback}
     */
    private final BCBeaconCallback beaconCallback = new BCBeaconCallback() {

        /**
         * {@link String} value of the beacon identifier
         *
         * @return {@link String} value of the beacon identifier
         */
        @NonNull
        @Override
        public String getBeaconCallbackIdentifier() {
            return MainActivity.class.getSimpleName();
        }

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
            BSLogHelper.log(MainActivity.this,
                    "onBeaconStartFailure",
                    null,
                    "Error code: " + errorCode);
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
            BSLogHelper.log(MainActivity.this,
                    "onBeaconStartSuccess",
                    null,
                    settings);

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
            BSLogHelper.log(MainActivity.this,
                    "onBeaconStopped",
                    null,
                    beacon);
        }

        /**
         * Method which provide the action when the beacon enter region
         *
         * @param region instance of the {@link Region}
         */
        @Override
        public void onBeaconEnterRegion(@NonNull Region region) {
            BSLogHelper.log(MainActivity.this,
                    "onBeaconEnterRegion",
                    null,
                    region);
        }

        /**
         * Method which provide the action when the beacon exit region
         *
         * @param region instance of the {@link Region}
         */
        @Override
        public void onBeaconExitRegion(@NonNull Region region) {
            BSLogHelper.log(MainActivity.this,
                    "onBeaconExitRegion",
                    null,
                    region);
        }

        /**
         * Method which provide the action when the beacon exit region
         *
         * @param region instance of the {@link Region}
         * @param state  {@link Integer} value of the state, could be MonitorNotifier.INSIDE
         */
        @Override
        public void onBeaconDetermineRegion(@NonNull Region region, int state) {
            BSLogHelper.log(MainActivity.this,
                    "onBeaconDetermineRegion",
                    null,
                    region);
        }

        /**
         * Method which provide the action when the beacons found in region
         *
         * @param region  instance of the {@link Region}
         * @param beacons {@link Collections} of the {@link Beacon}
         */
        @Override
        public void onBeaconInsideRegion(@NonNull Region region,
                                         @NonNull Collection<Beacon> beacons) {
            BSLogHelper.log(MainActivity.this,
                    "onBeaconInsideRegion",
                    null,
                    beacons);
        }

    };
}
