package org.wei.sptask.appService.locationService;

import android.location.Location;

/**
 * Created by admin on 12/9/2016.
 */
public interface LocationProvider {

    /**
     * @return The user's latest known location.
     */
    Location getCurrentLocation();

    /**
     * Start receiving regular location updates. Defaults to a battery-friendly update request.
     * @param listener The callback interface that will receive location updates.
     */
    void enableLocationUpdates(LocationUpdateListener listener);

    /**
     * Start receiving regular location updates.
     * @param listener The callback interface that will receive location updates.
     * @param requestMode Either LocationServicesManager.REQUEST_MODE_BATTERY or
     *                    LocationServicesManager.REQUEST_MODE_ACCURACY
     */
    void enableLocationUpdates(LocationUpdateListener listener, int requestMode);

    /**
     * Stop receiving regular location updates. This needs to be called once location updates are no
     * longer needed.
     */
    void disableLocationUpdates();

    /**
     * Asynchronous call to get the user's location.
     * @param listener The callback interface that will receive the location update.
     */
    void requestSingleUpdate(LocationUpdateListener listener);

    interface LocationUpdateListener {
        void onLocationChanged(Location location);
    }
}
