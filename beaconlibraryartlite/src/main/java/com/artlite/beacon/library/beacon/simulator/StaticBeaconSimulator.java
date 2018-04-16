package com.artlite.beacon.library.beacon.simulator;

import com.artlite.beacon.library.beacon.Beacon;

import java.util.List;

/**
 * Created by dyoung on 4/18/14.
 */
public class StaticBeaconSimulator implements BeaconSimulator {

    public List<Beacon> beacons = null;

    @Override
    public List<Beacon> getBeacons() {
        return beacons;
    }
    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }
}
