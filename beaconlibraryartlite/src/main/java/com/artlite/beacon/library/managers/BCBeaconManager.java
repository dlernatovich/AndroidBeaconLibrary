package com.artlite.beacon.library.managers;

import android.annotation.SuppressLint;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.artlite.beacon.library.beacon.Beacon;
import com.artlite.beacon.library.beacon.BeaconParser;
import com.artlite.beacon.library.beacon.BeaconTransmitter;
import com.artlite.beacon.library.callbacks.BCBeaconCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which provide the beacon manager functional
 */
public class BCBeaconManager {

    // CONSTANTS

    /**
     * {@link String} value of the beacon UUID
     */
    protected static String K_IBEACON_UUID = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    // INSTANCE

    /**
     * Instance of the {@link BCBeaconManager}
     */
    protected static BCBeaconManager instance;

    /**
     * Method which provide the getting of the instance of the {@link BCBeaconManager}
     *
     * @param context instance of the {@link Context}
     * @return instance of the {@link BCBeaconManager}
     */
    @NonNull
    protected static BCBeaconManager getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new BCBeaconManager(context);
        }
        return instance;
    }

    /**
     * Method which provide the getting of the instance of the {@link BCBeaconManager}
     *
     * @return instance of the {@link BCBeaconManager}
     */
    @Nullable
    protected static BCBeaconManager getInstance() {
        return instance;
    }

    // INIT

    /**
     * Method which provide the init of the {@link BCBeaconManager}
     *
     * @param context instance of the {@link Context}
     */
    public static void init(@NonNull Context context) {
        getInstance(context);
    }

    // VARIABLES

    /**
     * Instance of the {@link WeakReference}
     */
    private final WeakReference<Context> contextWeakReference;

    /**
     * Instance of the {@link BeaconTransmitter}
     */
    private BeaconTransmitter transmitter;

    /**
     * Instance of the {@link Beacon}
     */
    private Beacon beacon;

    /**
     * Instance of the {@link BeaconParser}
     */
    private BeaconParser parser;

    /**
     * {@link Map} of the {@link WeakReference} of the {@link BCBeaconCallback}
     */
    private final Map<String, WeakReference<BCBeaconCallback>> callbacks = new HashMap<>();

    // CONSTRUCTOR

    /**
     * Default constructor
     *
     * @param context instance of the {@link Context}
     */
    protected BCBeaconManager(@NonNull Context context) {
        this.contextWeakReference = new WeakReference<>(context);
    }

    // GET METHODS

    /**
     * Method which provide the context getting
     *
     * @return instance of the {@link Context}
     */
    @Nullable
    protected Context getContext() {
        if (this.contextWeakReference != null) {
            this.contextWeakReference.get();
        }
        return null;
    }

    /**
     * Method which provide the getting of the {@link List} of the exists callbacks
     *
     * @return instance of the {@link List}
     */
    protected List<BCBeaconCallback> getExistsCallbacks() {
        final Map<String, WeakReference<BCBeaconCallback>> map = new HashMap<>(this.callbacks);
        final List<BCBeaconCallback> result = new ArrayList<>();
        for (String key : map.keySet()) {
            final WeakReference<BCBeaconCallback> reference = map.get(key);
            final BCBeaconCallback callback = reference.get();
            if (callback != null) {
                result.add(callback);
            }
        }
        return result;
    }

    // ADD CALLBACK METHODS

    /**
     * Method which provide the add of the {@link BCBeaconCallback}
     *
     * @param callback instance of the {@link BCBeaconCallback}
     * @return {@link Boolean} value of the add callback result
     */
    public static boolean addCallback(@Nullable BCBeaconCallback callback) {
        if (instance != null) {
            return instance.addCallbackMethod(callback);
        }
        return false;
    }

    /**
     * Method which provide the add of the {@link BCBeaconCallback}
     *
     * @param callback instance of the {@link BCBeaconCallback}
     * @return {@link Boolean} value of the add callback result
     */
    protected boolean addCallbackMethod(@Nullable BCBeaconCallback callback) {
        try {
            this.callbacks.put(callback.getBeaconCallbackIdentifier(),
                    new WeakReference<>(callback));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // START BEACON

    /**
     * Method which provide the start {@link Beacon}
     *
     * @param minor    {@link Integer} value of the minor
     * @param major    {@link Integer} value of the major
     * @param callback instance of the {@link BCBeaconCallback}
     */
    @SuppressLint("NewApi")
    protected static void startBeacon(@IntRange(from = 0, to = 65535) int minor,
                                      @IntRange(from = 0, to = 65535) int major,
                                      @Nullable BCBeaconCallback callback) {
        if (instance != null) {
            instance.startBeaconMethod(minor, major, callback);
        }
    }

    /**
     * Method which provide the start {@link Beacon}
     *
     * @param minor    {@link Integer} value of the minor
     * @param major    {@link Integer} value of the major
     * @param callback instance of the {@link BCBeaconCallback}
     */
    @SuppressLint("NewApi")
    protected void startBeaconMethod(@IntRange(from = 0, to = 65535) int minor,
                                     @IntRange(from = 0, to = 65535) int major,
                                     @Nullable BCBeaconCallback callback) {
        // Add callback
        this.addCallbackMethod(callback);
        // UUID for beacon
        this.beacon = new Beacon.Builder().setId1(K_IBEACON_UUID)
                // Major for beacon
                .setId2(Integer.toString(major))
                // Minor for beacon
                .setId3(Integer.toString(minor))
                // Radius Networks.0x0118  Change this for other beacon layouts//0x004C for iPhone
                .setManufacturer(0x004C)
                // Power in dB
                .setTxPower(-56)
                // Remove this for beacon layouts without d: fields
                .setDataFields(Arrays.asList(new Long[]{0l}))
                .build();

        this.parser = new BeaconParser()
                .setBeaconLayout(K_IBEACON_LAYOUT);
        this.transmitter = new BeaconTransmitter(getContext(), this.parser);
        this.transmitter.startAdvertising(beacon, new AdvertiseCallback() {

            @Override
            public void onStartFailure(int error) {
                Log.e("Tag", "Advertisement start failed with code: " + error);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settings) {
                Log.e("Tag", "Advertisement start succeeded.");
            }
        });
    }

    // STOP BEACON METHODS

    /**
     * Method which provide the stop beacon method
     */
    protected void stopBeaconMethod() {
        if (this.transmitter != )
    }

}
