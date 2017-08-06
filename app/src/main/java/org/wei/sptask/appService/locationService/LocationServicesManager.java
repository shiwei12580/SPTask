package org.wei.sptask.appService.locationService;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.wei.sptask.SPTaskApplication;


/**
 * Created by admin on 12/9/2016.
 */
public class LocationServicesManager {

    public static final int REQUEST_MODE_BATTERY = 0;
    public static final int REQUEST_MODE_ACCURACY = 1;

    private static LocationServicesManager sInstance;

    private LocationProvider mLocationProvider;
    private Context mContext;

    private LocationServicesManager(Context context) {

        mContext = context;

        if (areGooglePlayServicesAvailable()) {
            mLocationProvider = new GoogleLocationProvider(context);
        } else {
            mLocationProvider = new AndroidLocationProvider(context);
        }

    }

    public Location getCurrentUserLocation() throws IllegalStateException {

        return mLocationProvider.getCurrentLocation();
    }

    /**
     * Check to see if Google Play Services are available
     *
     * @return true if Google Play Services are available
     */
    public boolean areGooglePlayServicesAvailable() {
        return (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext) ==
                ConnectionResult.SUCCESS);
    }

    public void requestSingleUpdate(LocationProvider.LocationUpdateListener listener) {
        mLocationProvider.requestSingleUpdate(listener);
    }

    public void requestUpdates(LocationProvider.LocationUpdateListener listener) {
        mLocationProvider.enableLocationUpdates(listener);
    }

    public void requestUpdates(LocationProvider.LocationUpdateListener listener, int requestMode) {
        mLocationProvider.enableLocationUpdates(listener, requestMode);
    }

    public void disableUpdates() {
        mLocationProvider.disableLocationUpdates();
    }

    /**
     * Helper function to determine if location services are enabled
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void init(Context context){
        if(sInstance == null) {
            sInstance = new LocationServicesManager(context);
        }
    }

    public static LocationServicesManager getInstance() {
        if (sInstance == null) {
            sInstance = new LocationServicesManager(SPTaskApplication.getInstance().getApplicationContext());
        }

        return sInstance;
    }
    /*
    public List<Address> searchAddress(String query) {
        return mLocationProvider.searchAddress(query);
    }
    */
}
