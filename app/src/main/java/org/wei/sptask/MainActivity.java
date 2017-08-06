package org.wei.sptask;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.wei.sptask.appService.locationService.LocationServicesManager;
import org.wei.sptask.appService.network.AsyncException;
import org.wei.sptask.appService.network.AsyncListener;
import org.wei.sptask.appService.network.AsyncToken;
import org.wei.sptask.appService.network.RequestManager;
import org.wei.sptask.appService.network.RequestManagerServiceConnection;
import org.wei.sptask.appService.network.request.PSIReadingRequest;
import org.wei.sptask.appService.network.response.GetPSIDataResponse;
import org.wei.sptask.data.ApiStatus;
import org.wei.sptask.data.ItemsData;
import org.wei.sptask.data.LocationData;
import org.wei.sptask.data.PSIData;
import org.wei.sptask.data.RegionData;
import org.wei.sptask.utils.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SwipeRefreshLayout mRefresher;
    private GoogleMap mMap;
    private RequestManagerServiceConnection mServiceConnection;
    private PSIData mData;
    private List<RegionData> mRegionData;
    private Location mCurrentLocation;
    private LocationDataController mController;
    private PSIDataRangeController mPSILevel;
    private boolean isMapReady;
    private Map<String, Marker> mMapMarkers;
    private Marker mCurrentLocationMarker;
    private List<Integer> PSILevelColor;
    private List<Integer> PSILevelImg;
    private List<Integer> PSILevelString;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRefresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(mRefreshListener);
        mRefresher.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.red,
                R.color.yellow);
        mServiceConnection = RequestManager.register(this);
        mController = new LocationDataController();
        mPSILevel = new PSIDataRangeController();
        mMapMarkers = new LinkedHashMap<String, Marker>();
        PSILevelColor = Arrays.asList(
                R.color.green,
                R.color.blue,
                R.color.yellow,
                R.color.orange,
                R.color.red
        );
        PSILevelImg = Arrays.asList(
                R.mipmap.good,
                R.mipmap.moderate,
                R.mipmap.unhealthy,
                R.mipmap.very_unhealthy,
                R.mipmap.hazardous
        );
        PSILevelString = Arrays.asList(
                R.string.good,
                R.string.moderate,
                R.string.unhealthy,
                R.string.very_unhealthy,
                R.string.hazardous

        );
    }

    @Override
    public void onStart() {
        super.onStart();
        mRefresher.setRefreshing(true);
        fetchCurrentReading();
    }

    private Location getDeviceLocation() {
        if (LocationServicesManager.getInstance().areGooglePlayServicesAvailable()) {
            try {
                mCurrentLocation = LocationServicesManager.getInstance().getCurrentUserLocation();
            } catch (IllegalStateException ignored) {
                //Nothing to do
            }
        }
        return mCurrentLocation;
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            fetchCurrentReading();
        }
    };

    private void fetchCurrentReading() {
        Date now = new Date();
        PSIReadingRequest request = new PSIReadingRequest(DateUtils.getDateTimeString(now), null);
        getDeviceLocation();
        mServiceConnection.processRequest(request, new AsyncListener<GetPSIDataResponse>() {
            @Override
            public void onResponse(GetPSIDataResponse response, AsyncToken token, AsyncException exception) {
                mRefresher.setRefreshing(false);
                if (response != null && exception == null) {
                    if (response.getApiStatus().getStatus().equals(ApiStatus.HEALTH)) {
                        ItemsData mLatest = response.getItemData().get(0);
                        mData = mLatest.getDataReading().getPSI24HourlyData();
                        mRegionData = response.getRegionData();
                        setupControllerData();
                        updateMarkers();

                    }
                }
            }
        });
    }

    private void setupControllerData() {
        LocationData east = null, west = null, central = null, north = null, south = null;
        for (int i = 0; i < mRegionData.size(); i++) {
            RegionData data = mRegionData.get(i);
            if (data.getName().equals("west")) {
                west = data.getLocationData();
            } else if (data.getName().equals("east")) {
                east = data.getLocationData();
            } else if (data.getName().equals("central")) {
                central = data.getLocationData();
            } else if (data.getName().equals("south")) {
                south = data.getLocationData();
            } else if (data.getName().equals("north")) {
                north = data.getLocationData();
            }
        }
        mController.setupRegions(west, east, central, south, north);

    }





    private void updateMarkers() {

        if (isMapReady) {
            for (int i = 0; i < mRegionData.size(); i++) {
                RegionData cur = mRegionData.get(i);

                if (mMapMarkers.containsKey(cur.getName())) {
                    // remove previous marker first.
                    mMapMarkers.get(cur.getName()).remove();
                }

                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_dialog, null);
                TextView psi_value = (TextView) marker.findViewById(R.id.psi_value);
                TextView location = (TextView) marker.findViewById(R.id.location);

                location.setText(cur.getName());
                if (cur.getName().equals("west")) {
                    psi_value.setText(String.valueOf((int) mData.getWestReading()));
                    int level = mPSILevel.getPSILevel(mData.getWestReading());
                    psi_value.setTextColor(ContextCompat.getColor(this,PSILevelColor.get(level)));
                } else if (cur.getName().equals("east")) {
                    psi_value.setText(String.valueOf((int) mData.getEastReading()));
                    int level = mPSILevel.getPSILevel(mData.getEastReading());
                    psi_value.setTextColor(ContextCompat.getColor(this,PSILevelColor.get(level)));
                } else if (cur.getName().equals("central")) {
                    psi_value.setText(String.valueOf((int) mData.getCentralReading()));
                    int level = mPSILevel.getPSILevel(mData.getCentralReading());
                    psi_value.setTextColor(ContextCompat.getColor(this,PSILevelColor.get(level)));
                } else if (cur.getName().equals("south")) {
                    psi_value.setText(String.valueOf((int) mData.getSouthReading()));
                    int level = mPSILevel.getPSILevel(mData.getSouthReading());
                    psi_value.setTextColor(ContextCompat.getColor(this,PSILevelColor.get(level)));
                } else if (cur.getName().equals("north")) {
                    psi_value.setText(String.valueOf((int) mData.getNorthReading()));
                    int level = mPSILevel.getPSILevel(mData.getNorthReading());
                    psi_value.setTextColor(ContextCompat.getColor(this,PSILevelColor.get(level)));
                }
                LatLng pos = new LatLng(cur.getLocationData().getLatitude(), cur.getLocationData().getLongtitude());

                Marker customMarker = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(marker))));

                mMapMarkers.put(cur.getName(), customMarker);
            }

            if (mCurrentLocation != null) {
                if(mCurrentLocationMarker != null){
                    mCurrentLocationMarker.remove();
                }
                LocationData mLocation = new LocationData(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                int region = mController.findMyRegion(mLocation);
                double mCurrentLocationPSIData = mController.getRegionReadings(region, mData);
                int level = mPSILevel.getPSILevel(mCurrentLocationPSIData);
                LatLng loc = new LatLng(mLocation.getLatitude(), mLocation.getLongtitude());
                mCurrentLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(getString(PSILevelString.get(level)))
                        .icon(BitmapDescriptorFactory.fromResource(PSILevelImg.get(level))));
                mCurrentLocationMarker.showInfoWindow();
            }
        }
    }

    public static Bitmap loadBitmapFromView(View v) {

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO call permission

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
        }

        LatLng central = new LatLng(1.35735, 103.82);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(central, 10.6f));
        // call location to get the location data cached first.
        getDeviceLocation();

        if (mRegionData != null && mData != null) {
            updateMarkers();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }
}
