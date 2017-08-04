package org.wei.sptask.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 4/8/2017.
 */

public class LocationData {
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longtitude;

    public LocationData(double lat, double lng) {
        latitude = lat;
        longtitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLatitude(double lat) {
        latitude = lat;
    }

    public void setLongtitude(double lng) {
        longtitude = lng;
    }
}
