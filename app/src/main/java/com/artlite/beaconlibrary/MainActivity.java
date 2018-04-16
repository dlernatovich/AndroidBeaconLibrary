package com.artlite.beaconlibrary;

import android.annotation.SuppressLint;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.artlite.beacon.library.beacon.Beacon;
import com.artlite.beacon.library.beacon.BeaconParser;
import com.artlite.beacon.library.beacon.BeaconTransmitter;
import com.artlite.bslibrary.annotations.FindViewBy;
import com.artlite.bslibrary.ui.activity.BSActivity;
import com.artlite.bslibrary.ui.fonted.BSEditText;

import java.util.Arrays;
import java.util.UUID;

//import Beacon;
//import BeaconManager;
//import BeaconParser;
//import BeaconTransmitter;

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
        final String generatedUUID = UUID.randomUUID().toString();
        Beacon beacon = new Beacon.Builder().setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6") // UUID for beacon
                .setId2(this.editMajor.getStringValue()) // Major for beacon
                .setId3(this.editMinor.getStringValue()) // Minor for beacon
                .setManufacturer(0x004C) // Radius Networks.0x0118  Change this for other beacon layouts//0x004C for iPhone
                .setTxPower(-56) // Power in dB
                .setDataFields(Arrays.asList(new Long[]{0l})) // Remove this for beacon layouts without d: fields
                .build();

//        "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6"

        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        this.transmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        this.transmitter.startAdvertising(beacon, new AdvertiseCallback() {

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("Tag", "Advertisement start failed with code: " + errorCode);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.e("Tag", "Advertisement start succeeded.");
            }
        });
    }

    /**
     * Method which provide the beacon stop
     */
    protected void stopBeacon() {
//        this.transmitter.stopAdvertising();
    }
}
