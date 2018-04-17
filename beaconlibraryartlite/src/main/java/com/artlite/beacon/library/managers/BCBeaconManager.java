package com.artlite.beacon.library.managers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.artlite.beacon.library.beacon.Beacon;
import com.artlite.beacon.library.beacon.BeaconConsumer;
import com.artlite.beacon.library.beacon.BeaconManager;
import com.artlite.beacon.library.beacon.BeaconParser;
import com.artlite.beacon.library.beacon.BeaconTransmitter;
import com.artlite.beacon.library.beacon.MonitorNotifier;
import com.artlite.beacon.library.beacon.RangeNotifier;
import com.artlite.beacon.library.beacon.Region;
import com.artlite.beacon.library.beacon.powersave.BackgroundPowerSaver;
import com.artlite.beacon.library.callbacks.BCBeaconCallback;
import com.artlite.beacon.library.callbacks.BCPermissionCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class which provide the beacon manager functional
 * HOW TO USE
 * 1. In application singleton add the BCBeaconManager.init(context: this);
 * In the launch activity
 * 1. Add in on create the method lines: BCBeaconManager.requestPermissions(this);
 */
public class BCBeaconManager implements BeaconConsumer, MonitorNotifier, RangeNotifier {

    // CONSTANTS

    /**
     * {@link String} value of the beacon UUID
     */
    protected static String K_IBEACON_UUID = "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_ALTBEACON_LAYOUT = "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_ALTBEACON2_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_EDDYSTONE_TLM_LAYOUT = "x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_EDDYSTONE_UID_LAYOUT = "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19";

    /**
     * {@link String} value of the beacon Layout
     */
    protected static String K_EDDYSTONE_URL_LAYOUT = "s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v";


    /**
     * {@link String} value of the tag
     */
    protected static String K_TAG = BCBeaconManager.class.getSimpleName();

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
     * Instance of the {@link BeaconManager}
     */
    private final BeaconManager beaconManager;

    /**
     * {@link Map} of the {@link WeakReference} of the {@link BCBeaconCallback}
     */
    private final Map<String, WeakReference<BCBeaconCallback>> callbacks = new HashMap<>();

    /**
     * Instance of the {@link BackgroundPowerSaver}
     */
    private final BackgroundPowerSaver backgroundPowerSaver;

    /**
     * Instance of the {@link Set}
     */
    private Set<Beacon> beacons = new HashSet<>();

    // CONSTRUCTOR

    /**
     * Default constructor
     *
     * @param context instance of the {@link Context}
     */
    protected BCBeaconManager(@NonNull Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        this.beaconManager = BeaconManager.getInstanceForApplication(context);
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(K_ALTBEACON2_LAYOUT));
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(K_IBEACON_LAYOUT));
        this.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
        this.beaconManager.bind(this);
        this.beaconManager.addMonitorNotifier(this);
        this.beaconManager.addRangeNotifier(this);
        this.backgroundPowerSaver = new BackgroundPowerSaver(context);
    }

    /**
     * Method which provide the destruction of the {@link BCBeaconManager}
     *
     * @throws Throwable instance of the {@link Throwable}
     */
    @Override
    protected void finalize() throws Throwable {
        this.beaconManager.unbind(this);
        super.finalize();
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
            return this.contextWeakReference.get();
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
    public boolean addCallbackMethod(@Nullable BCBeaconCallback callback) {
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
     */
    @SuppressLint("NewApi")
    public static void startBeacon() {
        startBeacon(null);
    }

    /**
     * Method which provide the start {@link Beacon}
     *
     * @param callback instance of the {@link BCBeaconCallback}
     */
    @SuppressLint("NewApi")
    public static void startBeacon(@Nullable BCBeaconCallback callback) {
        if (instance != null) {
            instance.startBeaconMethod(callback);
        }
    }

    /**
     * Method which provide the start {@link Beacon}
     *
     * @param callback instance of the {@link BCBeaconCallback}
     */
    @SuppressLint("NewApi")
    protected void startBeaconMethod(@Nullable BCBeaconCallback callback) {
        this.startBeaconMethod(BCRandomHelper.generateInt(0, 65535),
                BCRandomHelper.generateInt(0, 65535), callback);
    }

    /**
     * Method which provide the start {@link Beacon}
     *
     * @param minor    {@link Integer} value of the minor
     * @param major    {@link Integer} value of the major
     * @param callback instance of the {@link BCBeaconCallback}
     */
    @SuppressLint("NewApi")
    public static void startBeacon(@IntRange(from = 0, to = 65535) int minor,
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
        if (this.transmitter != null) {
            this.stopBeaconMethod();
        }
        // Check minor
        if ((minor < 0) || (minor > 65535)) {
            minor = BCRandomHelper.generateInt(0, 65535);
        }
        // Check major
        if ((major < 0) || (major > 65535)) {
            major = BCRandomHelper.generateInt(0, 65535);
        }
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
                BCBeaconManager.this.onStartFailure(error);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settings) {
                BCBeaconManager.this.onStartSuccess(settings);
            }
        });
    }

    // STOP BEACON METHODS

    /**
     * Method which provide the stop beacon method
     */
    public static void stopBeacon() {
        if (instance != null) {
            instance.stopBeaconMethod();
        }
    }

    /**
     * Method which provide the stop beacon method
     */
    protected void stopBeaconMethod() {
        if (this.transmitter != null) {
            if (this.transmitter.isStarted()) {
                this.transmitter.stopAdvertising();
            }
        }
        this.onBeaconStopped();
        this.transmitter = null;
        this.beacon = null;
        this.parser = null;
    }

    // PERMISSION CALLBACK

    /**
     * Method which provide the permission requests
     *
     * @param activity instance of the {@link Activity}
     */
    public static void requestPermissions(@Nullable Activity activity) {
        requestPermissions(activity, null);
    }

    /**
     * Method which provide the permission requests
     *
     * @param activity instance of the {@link Activity}
     * @param callback instance of the {@link BCPermissionCallback}
     */
    public static void requestPermissions(@Nullable Activity activity,
                                          @Nullable BCPermissionCallback callback) {
        if (instance != null) {
            instance.requestPermissionsMethod(activity, callback);
        }
    }

    /**
     * Method which provide the permission requests
     *
     * @param activity instance of the {@link Activity}
     * @param callback instance of the {@link BCPermissionCallback}
     */
    protected void requestPermissionsMethod(@Nullable Activity activity,
                                            @Nullable BCPermissionCallback callback) {
        BCPermissionHelper.requestPermissions(activity, callback,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    // CALLBACK METHODS

    /**
     * Method which provide the on start success functional
     *
     * @param settings instance of the {@link AdvertiseSettings}
     */
    protected void onStartSuccess(@Nullable AdvertiseSettings settings) {
        if ((this.beacon == null)
                || (this.parser == null)
                || (this.transmitter == null)
                || (settings == null)) {
            return;
        }
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconStartSuccess(this.beacon, this.parser, this.transmitter, settings);
        }
    }

    /**
     * Method which provide the on start success functional
     *
     * @param code instance of the {@link Integer}
     */
    protected void onStartFailure(int code) {
        if ((this.beacon == null)
                || (this.parser == null)
                || (this.transmitter == null)) {
            return;
        }
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconStartFailure(this.beacon, this.parser, this.transmitter, code);
        }
    }

    /**
     * Method which provide the functional when the beacon stopped
     */
    protected void onBeaconStopped() {
        if ((this.beacon == null)
                || (this.parser == null)
                || (this.transmitter == null)) {
            return;
        }
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconStopped(this.beacon, this.parser, this.transmitter);
        }
    }

    /**
     * Method which provide the action when the beacon enter the region
     *
     * @param region a Region that defines the criteria of beacons to look for
     */
    protected void onBeaconEnterRegion(@NonNull Region region) {
        if (region == null) {
            return;
        }
        Log.e(K_TAG, "onBeaconEnterRegion " + region);
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconEnterRegion(region);
        }
    }

    /**
     * Method which provide the action when the beacon enter the region
     *
     * @param region a Region that defines the criteria of beacons to look for
     */
    protected void onBeaconExitRegion(@NonNull Region region) {
        if (region == null) {
            return;
        }
        Log.e(K_TAG, "onBeaconExitRegion " + region);
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconExitRegion(region);
        }
    }

    /**
     * Method which provide the action when the beacon enter the region
     *
     * @param region a Region that defines the criteria of beacons to look for
     */
    protected void onBeaconDetermineRegion(@NonNull Region region, int state) {
        if (region == null) {
            return;
        }
        Log.e(K_TAG, "onBeaconDetermineRegion " + region);
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconDetermineRegion(region, state);
        }
    }

    /**
     * Method which provide the action when the beacon enter the region
     *
     * @param region a Region that defines the criteria of beacons to look for
     */
    protected void onBeaconInsideRegion(@NonNull Region region,
                                        @Nullable Collection<Beacon> beacons) {
        if ((region == null)
                || (beacons == null)) {
            return;
        }
        if (beacons.isEmpty()) {
            return;
        }
        int oldSize = this.beacons.size();
        int newSize = oldSize;
        this.beacons.addAll(beacons);
        newSize = this.beacons.size();
        if (oldSize == newSize) {
            return;
        }
        Log.e(K_TAG, "onBeaconInsideRegion " + beacons);
        final List<BCBeaconCallback> callbacks = this.getExistsCallbacks();
        for (BCBeaconCallback callback : callbacks) {
            callback.onBeaconInsideRegion(region, beacons);
        }
    }

    // BEACON CONSUMER METHODS

    /**
     * Called when the beacon service is running and ready to accept your commands through the
     * BeaconManager
     */
    @Override
    public void onBeaconServiceConnect() {
        Log.e(K_TAG, "onBeaconServiceConnect");
        final Region region = new Region("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF",
                null, null, null);
        try {
            this.beaconManager.startRangingBeaconsInRegion(region);
            this.beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (Exception ex) {
            Log.e(K_TAG, ex.toString());
        }
    }

    /**
     * Called by the BeaconManager to get the context of your Service or Activity.
     * This method is implemented by Service or Activity.
     * You generally should not override it.
     *
     * @return the application context of your service or activity
     */
    @Override
    public Context getApplicationContext() {
        Log.e(K_TAG, "getApplicationContext");
        return getContext();
    }

    /**
     * Called by the BeaconManager to unbind your BeaconConsumer to the  BeaconService.
     * This method is implemented by Service or Activity, and
     * You generally should not override it.
     *
     * @param connection
     * @return the application context of your service or activity
     */
    @Override
    public void unbindService(ServiceConnection connection) {
        Log.e(K_TAG, "unbindService");
    }

    /**
     * Called by the BeaconManager to bind your BeaconConsumer to the  BeaconService.
     * This method is implemented by Service or Activity, and
     * You generally should not override it.
     *
     * @param intent
     * @param connection
     * @param mode
     * @return the application context of your service or activity
     */
    @Override
    public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
        Log.e(K_TAG, "bindService");
        return false;
    }

    // MONITOR NOTIFIER

    /**
     * Called when at least one beacon in a <code>Region</code> is visible.
     *
     * @param region a Region that defines the criteria of beacons to look for
     */
    @Override
    public void didEnterRegion(Region region) {
        this.onBeaconEnterRegion(region);
    }

    /**
     * Called when no beacons in a <code>Region</code> are visible.
     *
     * @param region a Region that defines the criteria of beacons to look for
     */
    @Override
    public void didExitRegion(Region region) {
        this.onBeaconExitRegion(region);
    }

    /**
     * Called with a state value of MonitorNotifier.INSIDE when at least one
     * beacon in a <code>Region</code> is visible.
     * Called with a state value of MonitorNotifier.OUTSIDE when no beacons in
     * a <code>Region</code> are visible.
     *
     * @param state  either MonitorNotifier.INSIDE or MonitorNotifier.OUTSIDE
     * @param region a Region that defines the criteria of beacons to look for
     */
    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        this.onBeaconDetermineRegion(region, state);
    }

    // RANGE NOTIFIER

    /**
     * Called once per second to give an estimate of the mDistance to visible beacons
     *
     * @param beacons a collection of <code>Beacon<code> objects that have been
     *                seen in the past second
     * @param region  the <code>Region</code> object that defines the criteria
     *                for the ranged beacons
     */
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons,
                                        Region region) {
        this.onBeaconInsideRegion(region, beacons);
    }

    // GET BEACONS

    /**
     * Method which provide the getting {@link Beacon}
     *
     * @return instance of the {@link Beacon}
     */
    public Set<Beacon> getBeacons() {
        if (beacons == null) {
            beacons = new HashSet<>();
        }
        return beacons;
    }
}
