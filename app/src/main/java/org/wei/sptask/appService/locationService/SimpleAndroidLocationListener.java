package org.wei.sptask.appService.locationService;

import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by admin on 12/9/2016.
 */
public abstract class SimpleAndroidLocationListener implements LocationListener {
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
