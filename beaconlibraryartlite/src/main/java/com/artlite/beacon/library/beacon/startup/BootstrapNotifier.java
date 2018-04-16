package com.artlite.beacon.library.beacon.startup;

import android.content.Context;

import com.artlite.beacon.library.beacon.MonitorNotifier;

public interface BootstrapNotifier extends MonitorNotifier {
    public Context getApplicationContext();
}
