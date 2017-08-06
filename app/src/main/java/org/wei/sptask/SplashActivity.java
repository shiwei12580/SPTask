package org.wei.sptask;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import org.wei.sptask.appService.locationService.LocationServicesManager;

/**
 * Created by admin on 6/8/2017.
 */
public class SplashActivity extends AppCompatActivity {
    private RelativeLayout rootLayout;
    private static final int sleepTime = 2000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;

    private Location mCurrentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            if (LocationServicesManager.getInstance().areGooglePlayServicesAvailable()) {
                try {
                    // cache location data
                    mCurrentLocation = LocationServicesManager.getInstance().getCurrentUserLocation();
                    moveToMainActivity();
                } catch (IllegalStateException ignored) {
                    //Nothing to do
                }
            }
        }

    }

    private void moveToMainActivity(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                mCurrentLocation = LocationServicesManager.getInstance().getCurrentUserLocation();
            } else {
                // Permission was denied. Display an error message.
            }
            moveToMainActivity();
        }
    }

}
