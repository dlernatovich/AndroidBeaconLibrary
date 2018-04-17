package com.artlite.beaconlibrary;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.artlite.beacon.library.beacon.BeaconTransmitter;
import com.artlite.beacon.library.managers.BCBeaconManager;
import com.artlite.bslibrary.annotations.FindViewBy;
import com.artlite.bslibrary.ui.activity.BSActivity;
import com.artlite.bslibrary.ui.fonted.BSEditText;

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
        BCBeaconManager.requestPermissions(this);
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
        BCBeaconManager.startBeacon();
    }

    /**
     * Method which provide the beacon stop
     */
    protected void stopBeacon() {
        BCBeaconManager.stopBeacon();
    }

//    /**
//     * Instance of the {@link BCBeaconCallback}
//     */
//    private final BCBeaconRegionCallback beaconCallback = new BCBeaconRegionCallback() {
//
//        /**
//         * {@link String} value of the beacon identifier
//         *
//         * @return {@link String} value of the beacon identifier
//         */
//        @NonNull
//        @Override
//        public String getBeaconCallbackIdentifier() {
//            return MainActivity.class.getSimpleName();
//        }
//
//        /**
//         * Method which provide the action when the beacons found in region
//         *
//         * @param region  instance of the {@link Region}
//         * @param beacons {@link Collections} of the {@link Beacon}
//         */
//        @Override
//        public void onBeaconInsideRegion(@NonNull Region region, @NonNull Collection<Beacon> beacons) {
//            BSLogHelper.log(MainActivity.this,
//                    "onBeaconInsideRegion",
//                    null,
//                    beacons);
//        }
//
//    };
}
