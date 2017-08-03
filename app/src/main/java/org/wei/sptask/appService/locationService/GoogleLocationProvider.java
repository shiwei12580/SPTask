package org.wei.sptask.appService.locationService;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 12/9/2016.
 */
@SuppressWarnings("MissingPermission")
public class GoogleLocationProvider implements LocationProvider, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long REQUEST_MODE_BATTERY_UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(1);
    private static final long REQUEST_MODE_BATTERY_FASTEST_INTERVAL = REQUEST_MODE_BATTERY_UPDATE_INTERVAL/3;

    private static final long REQUEST_MODE_ACCURACY_UPDATE_INTERVAL = TimeUnit.SECONDS.toMillis(15);
    private static final long REQUEST_MODE_ACCURACY_FASTEST_INTERVAL = REQUEST_MODE_ACCURACY_UPDATE_INTERVAL/2;

    private GoogleApiClient mClient;
    private Location mLocation;
    private LocationUpdateListener mListener;
    private LocationRequest mPendingRequest;

    private LocationListener mGoogleLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }
        }
    };


    public GoogleLocationProvider(Context context) {
        mClient = new GoogleApiClient.Builder(context, this, this).addApi(LocationServices.API).build();
        mClient.connect();
    }

    @Override
    public Location getCurrentLocation() {
        if (mClient.isConnected()) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        }

        return mLocation;
    }

    @Override
    public void enableLocationUpdates(LocationUpdateListener listener) {
        enableLocationUpdates(listener, LocationServicesManager.REQUEST_MODE_BATTERY);
    }

    @Override
    public void enableLocationUpdates(LocationUpdateListener listener, int requestMode) {
        LocationRequest request;

        if (requestMode == LocationServicesManager.REQUEST_MODE_BATTERY) {
            request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(REQUEST_MODE_BATTERY_UPDATE_INTERVAL)
                    .setFastestInterval(REQUEST_MODE_BATTERY_FASTEST_INTERVAL);

        } else if (requestMode == LocationServicesManager.REQUEST_MODE_ACCURACY) {
            request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(REQUEST_MODE_ACCURACY_UPDATE_INTERVAL)
                    .setFastestInterval(REQUEST_MODE_ACCURACY_FASTEST_INTERVAL);

        } else {
            request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(REQUEST_MODE_BATTERY_UPDATE_INTERVAL)
                    .setFastestInterval(REQUEST_MODE_BATTERY_FASTEST_INTERVAL);
        }

        requestUpdates(listener, request);
    }

    @Override
    public void disableLocationUpdates() {
        mListener = null;
        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, mGoogleLocationListener);
    }

    @Override
    public void requestSingleUpdate(LocationUpdateListener listener) {

        LocationRequest request = LocationRequest.create()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0);

        requestUpdates(listener, request);
    }

    private void requestUpdates(LocationUpdateListener listener, LocationRequest request) {
        mListener = listener;
        if (mClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, mGoogleLocationListener);
        } else {
            mPendingRequest = request;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mPendingRequest != null) {
            requestUpdates(mListener, mPendingRequest);
            mPendingRequest = null;
        }

        if (mListener != null && mLocation != null) {
            mListener.onLocationChanged(mLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}