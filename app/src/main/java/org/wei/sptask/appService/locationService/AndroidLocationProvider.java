package org.wei.sptask.appService.locationService;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 12/9/2016.
 */
@SuppressWarnings("MissingPermission")
public class AndroidLocationProvider implements LocationProvider {

    private final static long REQUEST_MODE_BATTERY_MIN_TIME = TimeUnit.MINUTES.toMillis(1);
    private final static long REQUEST_MODE_BATTERY_MIN_DISTANCE = 100;

    private final static long REQUEST_MODE_ACCURACY_MIN_TIME = TimeUnit.SECONDS.toMillis(15);
    private final static long REQUEST_MODE_ACCURACY_MIN_DISTANCE = 10;

    private LocationManager mLocationManager;
    private LocationUpdateListener mListener;
    private Location mLocation;

    private SimpleAndroidLocationListener mAndroidLocationListener = new SimpleAndroidLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }
        }
    };

    public AndroidLocationProvider(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public Location getCurrentLocation() {

        // Does last known location make sense?
        long minTime = new Date().getTime() - (5 * 60 * 1000); // 5 minutes ago
        long bestTime = 0;
        float bestAccuracy = 1500.0f; // meters of accuracy (approx 1 mile)

        Location bestResult = null;
        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if (time > minTime && accuracy < bestAccuracy) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        if (bestResult != null) {
            mLocation = bestResult;
        }

        return mLocation;
    }

    @Override
    public void enableLocationUpdates(LocationUpdateListener listener) {
        enableLocationUpdates(listener, LocationServicesManager.REQUEST_MODE_BATTERY);
    }

    @Override
    public void enableLocationUpdates(LocationUpdateListener listener, int requestMode) {
        mListener = listener;
        Criteria criteria = new Criteria();
        long minTime, minDistance;

        if (requestMode == LocationServicesManager.REQUEST_MODE_BATTERY) {
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

            minTime = REQUEST_MODE_BATTERY_MIN_TIME;
            minDistance = REQUEST_MODE_BATTERY_MIN_DISTANCE;

        } else if (requestMode == LocationServicesManager.REQUEST_MODE_ACCURACY) {
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);

            minTime = REQUEST_MODE_ACCURACY_MIN_TIME;
            minDistance = REQUEST_MODE_ACCURACY_MIN_DISTANCE;

        } else {
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

            minTime = REQUEST_MODE_BATTERY_MIN_TIME;
            minDistance = REQUEST_MODE_BATTERY_MIN_DISTANCE;

        }

        String provider = mLocationManager.getBestProvider(criteria, true);
        if (provider != null) {
            mLocationManager.requestLocationUpdates(provider, minTime, minDistance, mAndroidLocationListener);
        }
    }

    @Override
    public void disableLocationUpdates() {
        mLocationManager.removeUpdates(mAndroidLocationListener);
    }

    @Override
    public void requestSingleUpdate(LocationUpdateListener listener) {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        mLocationManager.requestSingleUpdate(criteria, mAndroidLocationListener, Looper.getMainLooper());
    }
}
