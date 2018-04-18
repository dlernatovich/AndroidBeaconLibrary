# AndroidBeaconLibrary
# Quick Start
## Gradle

Add to the app/build.gradle this lines:

```gradle
// Artlite libraries
repositories {
    maven {
        url "https://dl.bintray.com/darknessbeast/beaconproject-artlite"
    }
}

dependencies {
    ...
    
    compile 'com.artlite:beaconlibrary:1.0'

    ...
}
```

## Application singleton

In application singleton add this lines:

```java
/**
 * CLass which provide the application functional
 * Created by dlernatovich on 1/4/18.
 */
public final class CurrentApplication extends MultiDexApplication {

    /**
     * Instance of the {@link BCBeaconRegionCallback}
     */
    private BCBeaconRegionCallback regionCallback;

    /**
     * Method which provide the action when the application created
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.onInitLibraries();
    }

    /**
     * Method which provide the init of the libraries
     */
    protected void onInitLibraries() {
        BCBeaconManager.init(this);
        BCBeaconManager.addCallback(this.getRegionCallback());
    }

    /**
     * Method which provide the getting of the {@link BCBeaconRegionCallback}
     *
     * @return instance of the {@link BCBeaconRegionCallback}
     */
    @NonNull
    protected BCBeaconRegionCallback getRegionCallback() {
        if (this.regionCallback == null) {
            this.regionCallback = new BCBeaconRegionCallback() {
                /**
                 * {@link String} value of the beacon identifier
                 *
                 * @return {@link String} value of the beacon identifier
                 */
                @NonNull
                @Override
                public String getBeaconCallbackIdentifier() {
                    return CurrentApplication.class.getSimpleName();
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
                    // TODO Notifications here about finding the beacons
                }
            };
        }
        return this.regionCallback;
    }
}
```
## Start activity

For working of the library you need to reqest of the permission, like this:

```java
/**
 * Class which provide the home functionality
 */
public class HomeActivity extends BSActivity {

    //...

    /**
     * Method which provide the action when Activity is created
     *
     * @param bundle
     */
    @Override
    protected void onCreateActivity(@Nullable Bundle bundle) {
        BCBeaconManager.requestPermissions(this);
    }

    //...

}
```

## Start broadcast

If you want to start the broadcast use this lines:

```java
    /**
     * Method which provide the beacon starting
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void startBeacon() {
        BCBeaconManager.startBeacon();
    }
```

# Reference

This library based on the [AltBeacon library](https://github.com/AltBeacon/android-beacon-library). This is only simplifying of the library.
